package game;

import engine.core.GameLogic;
import engine.input.Input;
import engine.math.Matrix4f;
import engine.render.Mesh;
import engine.render.Renderer;
import engine.render.ShaderProgram;

public class MyGame implements GameLogic {
    private Renderer renderer;
    private Mesh triangle;
    private ShaderProgram shader;
    private Matrix4f transform;

    private float x = 0f;

    @Override
    public void init() {
        renderer = new Renderer();

        float[] positions = {
                -0.5f, -0.5f,
                 0.5f, -0.5f,
                 0.0f,  0.5f
        };
        triangle = new Mesh(positions, 2);

        shader = new ShaderProgram("/shaders/triangle.vert", "/shaders/triangle.frag");

        transform = new Matrix4f();
    }

    @Override
    public void update(double dt, Input input) {
        x += dt * 0.5f;

        transform.identity().translate(x, 0f, 0f).scale(0.5f);
    }

    @Override
    public void render() {
        renderer.render(triangle, shader, transform);
    }

    @Override
    public void cleanup() {
        if (triangle != null) { triangle.cleanup(); }
        if (shader != null) { shader.cleanup(); }
    }
}
