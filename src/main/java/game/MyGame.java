package game;

import engine.core.GameLogic;
import engine.input.Input;
import engine.math.Matrix4f;
import engine.render.Mesh;
import engine.render.Renderer;
import engine.render.ShaderProgram;
import engine.scene.Camera;

public class MyGame implements GameLogic {
    private static final float[] CUBE_VERTICES = {
            // 0
            -0.5f, -0.5f, -0.5f,
            // 1
            0.5f, -0.5f, -0.5f,
            // 2
            0.5f,  0.5f, -0.5f,
            // 3
            -0.5f,  0.5f, -0.5f,

            // 4
            -0.5f, -0.5f,  0.5f,
            // 5
            0.5f, -0.5f,  0.5f,
            // 6
            0.5f,  0.5f,  0.5f,
            // 7
            -0.5f,  0.5f,  0.5f
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

    private Matrix4f model;
    private Matrix4f projection;

    private float rotation = 0f;

    @Override
    public void init() {
        renderer = new Renderer();

        cube = new Mesh(CUBE_VERTICES, 3, CUBE_INDICES);

        shader = new ShaderProgram("/shaders/basic.vert", "/shaders/basic.frag");

        camera = new Camera();
        model = new Matrix4f();

        projection = Matrix4f.perspective(70f, 1280f / 720f, 0.1f, 100f);
    }

    @Override
    public void update(double dt, Input input) {
        rotation += dt;

        Matrix4f rotX = Matrix4f.rotationX(rotation);
        Matrix4f rotY = Matrix4f.rotationY(rotation);

        model = rotX.mul(rotY);
    }

    @Override
    public void render() {
        renderer.render(cube, shader, model, camera.getViewMatrix(), projection);
    }

    @Override
    public void cleanup() {
        if (cube != null) { cube.cleanup(); }
        if (shader != null) { shader.cleanup(); }
    }
}
