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

public class VoxelGame implements ScreenLogic {
    private Renderer renderer;
    private ShaderProgram shader;
    private Camera camera;

    private ChunkMesh chunkMesh;
    private Texture atlasTexture;
    private TextureAtlas atlas;

    private Matrix4f projection;

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
        projection = new Matrix4f().perspective((float) Math.toRadians(70f), 1280f / 720f, 0.1f, 100f);
        Log.debug("VoxelGame", "Projection: fov=70 aspect=%.3f", 1280f / 720f);

        Chunk chunk = new Chunk(new ChunkPos(0, 0));
        Log.info("VoxelGame", "Created chunk at %s", chunk.getPos());

        for (int x = 0; x < Chunk.SIZE_X; x++) {
            for (int z = 0; z < Chunk.SIZE_Z; z++) {
                chunk.setBlock(x, 0, z, BlockType.GRASS);
            }
        }

        chunk.setBlock(5, 1, 0, BlockType.DIRT);
        chunk.setBlock(5, 1, 1, BlockType.STONE);
        chunk.setBlock(5, 2, 2, BlockType.STONE);

        chunkMesh = ChunkMeshBuilder.build(chunk, atlas);
        Log.info("VoxelGame", "Chunk mesh built");
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
        renderer.render(chunkMesh.getMesh(), shader, new Matrix4f(), camera.getViewMatrix(), projection);
    }

    @Override
    public void cleanup() {
        Log.info("VoxelGame", "cleanup()");
        if (chunkMesh != null) { chunkMesh.cleanup(); }
        if (shader != null) { shader.cleanup(); }
        if (atlasTexture != null) { atlasTexture.cleanup(); }
    }

    private void rebuildProjection(Game game) {
        int w = game.getWindow().getFbWidth();
        int h = game.getWindow().getFbHeight();
        float aspect = (h == 0) ? 1f : (float) w / (float) h;

        projection = new Matrix4f().perspective((float) Math.toRadians(70f), aspect, 0.1f, 100f);
    }
}
