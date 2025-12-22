package game;

import engine.core.GameLogic;
import engine.input.Input;
import engine.render.Mesh;
import engine.render.Renderer;
import engine.render.ShaderProgram;


import static org.lwjgl.glfw.GLFW.*;

public class MyGame implements GameLogic {
    private Renderer renderer;
    private Mesh triangle;
    private ShaderProgram shader;

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
    }

    @Override
    public void update(double dt, Input input) {
        /*
        if (input.isKeyPressed(GLFW_KEY_SPACE)) {
            System.out.println("You pressed SPACE");
        }
         */
    }

    @Override
    public void render() {
        renderer.render(triangle, shader);
    }

    @Override
    public void cleanup() {
        if (triangle != null) { triangle.cleanup(); }
        if (shader != null) { shader.cleanup(); }
    }
}
