package game.logic;

import engine.core.Game;
import engine.input.Action;
import engine.input.Input;
import engine.render.Texture;
import engine.render.TextureAtlas;
import engine.util.Log;
import engine.world.*;
import game.screens.MainMenuScreen;
import org.joml.Matrix4f;
import engine.render.Renderer;
import engine.render.ShaderProgram;
import engine.scene.Camera;

import java.util.HashMap;
import java.util.Map;

public class VoxelGame implements ScreenLogic {
    private Renderer renderer;
    private ShaderProgram shader;
    private Camera camera;

    private ChunkMesh chunkMesh;
    private Texture atlasTexture;
    private TextureAtlas atlas;

    private Matrix4f projection;

    private World world;
    private final Map<ChunkPos, ChunkMesh> chunkMeshes = new HashMap<>();
    private final Map<ChunkPos, Matrix4f> chunkModels = new HashMap<>();

    private static final float MOUSE_SENS = 0.1f;
    private static final float MOVE_SPEED = 4.0f;

    @Override
    public void init(Game game) {
        renderer = new Renderer();
        shader = new ShaderProgram("/shaders/atlas.vert", "/shaders/atlas.frag");
        atlasTexture = new Texture("/textures/atlas.png");
        atlas = new TextureAtlas(16, 16);
        Log.info("VoxelGame", "Loaded shader atlas + texture atlas.png");

        camera = new Camera();

        int w = game.getWindow().getFbWidth();
        int h = game.getWindow().getFbHeight();
        float aspect = (float) w / (float) h;
        projection = new Matrix4f().perspective((float) Math.toRadians(70f), aspect, 0.1f, 100f);
        Log.debug("VoxelGame", "Projection: fov=70 aspect=%.3f", aspect);

        world = new World();

        int radius = 1;
        for (int cz = -radius; cz <= radius; cz++) {
            for (int cx = -radius; cx <= radius; cx++) {

                ChunkPos pos = new ChunkPos(cx, cz);
                Chunk chunk = new Chunk(pos);

                for (int x = 0; x < Chunk.SIZE_X; x++) {
                    for (int z = 0; z < Chunk.SIZE_Z; z++) {
                        chunk.setBlock(x, 0, z, BlockType.GRASS);
                    }
                }

                int px = (cx + 10) % 16;
                int pz = (cz + 10) % 16;
                chunk.setBlock(px, 1, pz, BlockType.STONE);
                chunk.setBlock(px, 2, pz, BlockType.STONE);

                world.putChunk(chunk);
                Log.info("World", "Created chunk at %s", pos);
            }
        }

        rebuildAllChunkMeshes();
    }

    @Override
    public void onResize(Game game) {
        rebuildProjection(game);
    }

    @Override
    public void handleInput(Game game, Input input) {
        if (input.isActionDown(Action.EXIT)) {
            Log.info("VoxelGame", "EXIT pressed -> MainMenuScreen");
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void update(double dt, Input input) {
        float v = (float) dt * MOVE_SPEED;

        if (input.isActionDown(Action.MOVE_FORWARD))    { camera.addMoveForward(); }
        if (input.isActionDown(Action.MOVE_BACKWARD))   { camera.addMoveBackward(); }
        if (input.isActionDown(Action.MOVE_RIGHT))      { camera.addMoveRight(); }
        if (input.isActionDown(Action.MOVE_LEFT))       { camera.addMoveLeft(); }
        if (input.isActionDown(Action.MOVE_UP))         { camera.addMoveUp(); }
        if (input.isActionDown(Action.MOVE_DOWN))       { camera.addMoveDown(); }

        camera.move(v);
    }

    @Override
    public void render(Game game, Input input) {
        atlasTexture.bind(0);
        shader.bind();
        shader.setUniform1i("uAtlas", 0);
        shader.unbind();

        float dx = (float) input.consumeMouseDeltaX();
        float dy = (float) input.consumeMouseDeltaY();

        camera.addYawPitch(dx * MOUSE_SENS, -dy * MOUSE_SENS);

        Matrix4f view = camera.getViewMatrix();
        for (var entry : chunkMeshes.entrySet()) {
            ChunkPos pos = entry.getKey();
            ChunkMesh mesh = entry.getValue();
            Matrix4f model = chunkModels.get(pos);

            renderer.render(mesh.getMesh(), shader, model, view, projection);
        }
    }

    @Override
    public void cleanup() {
        Log.info("VoxelGame", "cleanup()");
        for (ChunkMesh m : chunkMeshes.values()) { m.cleanup(); }
        chunkMeshes.clear();
        chunkModels.clear();
        if (shader != null) { shader.cleanup(); }
        if (atlasTexture != null) { atlasTexture.cleanup(); }
    }

    private void rebuildProjection(Game game) {
        int w = game.getWindow().getFbWidth();
        int h = game.getWindow().getFbHeight();
        float aspect = (h == 0) ? 1f : (float) w / (float) h;

        projection.identity().perspective((float) Math.toRadians(70f), aspect, 0.1f, 100f);
    }

    private void rebuildAllChunkMeshes() {
        for (ChunkMesh m : chunkMeshes.values()) {
            m.cleanup();
        }
        chunkMeshes.clear();
        chunkModels.clear();

        for (var entry : world.getChunks().entrySet()) {
            ChunkPos pos = entry.getKey();
            Chunk chunk = entry.getValue();

            ChunkMesh mesh = ChunkMeshBuilder.build(world, chunk, atlas);
            chunkMeshes.put(pos, mesh);

            Matrix4f model = new Matrix4f().translation(
                    pos.x() * Chunk.SIZE_X,
                    0,
                    pos.z() * Chunk.SIZE_Z
            );
            chunkModels.put(pos, model);
        }

        Log.info("VoxelGame", "Rebuilt meshes: %d chunks", chunkMeshes.size());
    }
}
