package game;

import engine.core.GameLogic;
import engine.input.Action;
import engine.input.Input;
import engine.world.*;
import org.joml.Matrix4f;
import engine.render.Mesh;
import engine.render.Renderer;
import engine.render.ShaderProgram;
import engine.scene.Camera;

public class MyGame implements GameLogic {
    private static final float[] CUBE_VERTICES = {
            -0.5f, -0.5f, -0.5f,    0, 0, 1,   1, 0, 0,
             0.5f, -0.5f, -0.5f,    0, 0, 1,   1, 0, 0,
             0.5f,  0.5f, -0.5f,    0, 0, 1,   1, 0, 0,
            -0.5f,  0.5f, -0.5f,    0, 0, 1,   1, 0, 0,
            -0.5f, -0.5f,  0.5f,    0, 0, 1,   1, 0, 0,
             0.5f, -0.5f,  0.5f,    0, 0, 1,   1, 0, 0,
             0.5f,  0.5f,  0.5f,    0, 0, 1,   1, 0, 0,
            -0.5f,  0.5f,  0.5f,    0, 0, 1,   1, 0, 0
    };
    private static final int[] CUBE_INDICES = {
            // Front (z = +0.5)  4,5,6,7
            4, 5, 6,
            6, 7, 4,

            // Back (z = -0.5)   0,1,2,3
            1, 0, 3,
            3, 2, 1,

            // Left (x = -0.5)   0,4,7,3
            0, 4, 7,
            7, 3, 0,

            // Right (x = +0.5)  5,1,2,6
            5, 1, 2,
            2, 6, 5,

            // Top (y = +0.5)    3,7,6,2
            3, 7, 6,
            6, 2, 3,

            // Bottom (y = -0.5) 0,1,5,4
            0, 1, 5,
            5, 4, 0
    };


    private Renderer renderer;
    private Mesh cube;
    private ShaderProgram shader;
    private Camera camera;
    private Chunk chunk;
    private ChunkMesh chunkMesh;

    private Matrix4f model;
    private Matrix4f projection;

    //private Block[][] world;
    private static final int WORLD_SIZE = 7;

    private static final float MOUSE_SENS = 0.1f;
    private static final float MOVE_SPEED = 3.0f;

    @Override
    public void init() {
        renderer = new Renderer();

        cube = new Mesh(CUBE_VERTICES, CUBE_INDICES);

        shader = new ShaderProgram("/shaders/light.vert", "/shaders/light.frag");

        camera = new Camera();
        model = new Matrix4f();
        projection = new Matrix4f().perspective((float) Math.toRadians(70f), 1280f / 720f, 0.1f, 100f);

        /*
        world = new Block[WORLD_SIZE][WORLD_SIZE];

        for (int x = 0; x < WORLD_SIZE; x++) {
            for (int z = 0; z < WORLD_SIZE; z++) {
                world[x][z] = new Block(BlockType.GRASS);
            }
        }
         */
        chunk = new Chunk(new ChunkPos(0, 0));

        for (int x = 0; x < Chunk.SIZE_X; x++) {
            for (int z = 0; z < Chunk.SIZE_Z; z++) {
                chunk.setBlock(x, 0, z, BlockType.GRASS);
            }
        }

        chunk.setBlock(5, 1, 0, BlockType.STONE);
        chunk.setBlock(5, 1, 1, BlockType.STONE);
        chunk.setBlock(5, 2, 2, BlockType.STONE);

        chunkMesh = ChunkMeshBuilder.build(chunk);
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
    public void render(Input input) {
        float dx = (float) input.consumeMouseDeltaX();
        float dy = (float) input.consumeMouseDeltaY();

        camera.addYawPitch(dx * MOUSE_SENS, -dy * MOUSE_SENS);

        /*
        for (int x = 0; x < WORLD_SIZE; x++) {
            for (int z = 0; z < WORLD_SIZE; z++) {
                Block block = world[x][z];

                if (block.getType() == BlockType.AIR) { continue; }

                Matrix4f model = new Matrix4f().translation(x, 0, z);

                renderer.render(cube, shader, model, camera.getViewMatrix(), projection);
            }
        }
         */
        renderer.render(chunkMesh.getMesh(), shader, model, camera.getViewMatrix(), projection);


    }

    @Override
    public void cleanup() {
        if (cube != null) { cube.cleanup(); }
        if (shader != null) { shader.cleanup(); }
    }
}
