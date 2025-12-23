package game;

import engine.core.GameLogic;
import engine.input.Input;
import engine.math.Matrix4f;
import engine.render.Mesh;
import engine.render.Renderer;
import engine.render.ShaderProgram;
import engine.scene.Camera;
import engine.world.Transform;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

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

    private Matrix4f model;
    private Matrix4f projection;

    private List<Transform> blocks = new ArrayList<>();

    private static final float MOUSE_SENS = 0.1f;
    private static final float MOVE_SPEED = 3.0f;

    private float rotationTime = 0f;

    @Override
    public void init() {
        renderer = new Renderer();

        cube = new Mesh(CUBE_VERTICES, CUBE_INDICES);

        shader = new ShaderProgram("/shaders/light.vert", "/shaders/light.frag");

        camera = new Camera();
        model = new Matrix4f();
        projection = Matrix4f.perspective(70f, 1280f / 720f, 0.1f, 100f);

        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                blocks.add(new Transform(x, 0, z));
            }
        }
    }

    @Override
    public void update(double dt, Input input) {
        float dx = (float) input.consumeMouseDeltaX();
        float dy = (float) input.consumeMouseDeltaY();

        camera.addYawPitch(dx * MOUSE_SENS, -dy * MOUSE_SENS);

        float v = (float) dt * MOVE_SPEED;

        if (input.isKeyDown(GLFW_KEY_W)) {
            camera.move(camera.getForwardXZ().mul(v));
        }
        if (input.isKeyDown(GLFW_KEY_S)) {
            camera.move(camera.getForwardXZ().mul(-v));
        }
        if (input.isKeyDown(GLFW_KEY_D)) {
            camera.move(camera.getRight().mul(v));
        }
        if (input.isKeyDown(GLFW_KEY_A)) {
            camera.move(camera.getRight().mul(-v));
        }

        if (input.isKeyDown(GLFW_KEY_SPACE)) {
            camera.move(camera.getUp().mul(v));
        }
        if (input.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            camera.move(camera.getUp().mul(-v));
        }

        rotationTime += dt;

        Matrix4f rotX = Matrix4f.rotationX(rotationTime);
        Matrix4f rotY = Matrix4f.rotationY(rotationTime);

        model = rotX.mul(rotY);
    }

    @Override
    public void render() {
        for (Transform t : blocks) {
            renderer.render(cube, shader, t.toModelMatrix(), camera.getViewMatrix(), projection);
        }


    }

    @Override
    public void cleanup() {
        if (cube != null) { cube.cleanup(); }
        if (shader != null) { shader.cleanup(); }
    }
}
