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
            // Front
            -0.5f, -0.5f,  0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,

            // Back
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,

            // Left
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,

            // Right
            0.5f, -0.5f,  0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f,  0.5f,  0.5f,
            0.5f, -0.5f,  0.5f,

            // Top
            -0.5f,  0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f,  0.5f,

            // Bottom
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f
    };

    private Renderer renderer;
    private Mesh triangle;
    private ShaderProgram shader;
    private Camera camera;

    private Matrix4f model;
    private Matrix4f projection;

    private float rotation = 0f;

    @Override
    public void init() {
        renderer = new Renderer();

        triangle = new Mesh(CUBE_VERTICES, 3);

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
        renderer.render(triangle, shader, model, camera.getViewMatrix(), projection);
    }

    @Override
    public void cleanup() {
        if (triangle != null) { triangle.cleanup(); }
        if (shader != null) { shader.cleanup(); }
    }
}
