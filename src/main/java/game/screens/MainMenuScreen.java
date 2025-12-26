package game.screens;

import engine.core.Game;
import engine.input.Action;
import engine.input.Input;
import engine.render.Mesh;
import engine.render.Renderer;
import engine.render.ShaderProgram;
import engine.screen.Screen;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MainMenuScreen implements Screen {

    private final Game game;
    private Renderer renderer;
    private ShaderProgram shader;
    private Mesh buttonMesh;

    private Rect startButton;
    private Rect quitButton;

    public MainMenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void init() {
        game.getWindow().setCursorCaptured(false);

        renderer = new Renderer();
        shader = new ShaderProgram("/shaders/menu.vert", "/shaders/menu.frag");

        createButtonMesh();
        calculateLayout();
    }

    @Override
    public void handleInput(Input input) {
        if (game.getWindow().isResize()) {
            game.getWindow().setResize(false);
            calculateLayout();
        }

        if (input.isActionPressed(Action.LEFT_CLICK)) {
            float mx = (float) input.getMouseX();
            float my = (float) input.getMouseY();

            if (startButton.contains(mx, my)) {
                game.setScreen(new GameScreen(game));
            } else if (quitButton.contains(mx, my)) {
                game.getWindow().close();
            }
        }
    }

    @Override
    public void update(double dt, Input input) {

    }

    @Override
    public void render(Input input) {
        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_CULL_FACE);

        int w = game.getWindow().getFbWidth();
        int h = game.getWindow().getFbHeight();
        Matrix4f ortho = new Matrix4f().setOrtho(0, w, h, 0, -1, 1);

        float mx = (float) input.getMouseX();
        float my = (float) input.getMouseY();

        Vector3f colorStart = (startButton.contains(mx, my)) ?
                new Vector3f(0.7f, 0.7f, 0.7f) : new Vector3f(0.4f, 0.4f, 0.4f);
        Vector3f colorQuit = (quitButton.contains(mx, my)) ?
                new Vector3f(0.7f, 0.7f, 0.7f) : new Vector3f(0.4f, 0.4f, 0.4f);

        drawButton(startButton, ortho, colorStart);
        drawButton(quitButton, ortho, colorQuit);

        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_CULL_FACE);
    }

    @Override
    public void cleanup() {
        if (buttonMesh != null) buttonMesh.cleanup();
        if (shader != null) shader.cleanup();
    }

    private void createButtonMesh() {
        float[] vertices = {
                0, 0, 0,  0, 0, 1,  0, 0, // Top Left
                1, 0, 0,  0, 0, 1,  1, 0, // Top Right
                1, 1, 0,  0, 0, 1,  1, 1, // Bottom Right
                0, 1, 0,  0, 0, 1,  0, 1  // Bottom Left
        };
        int[] indices = { 0, 1, 2, 2, 3, 0 };
        buttonMesh = new Mesh(vertices, indices);
    }

    private void calculateLayout() {
        int w = game.getWindow().getFbWidth();
        int h = game.getWindow().getFbHeight();

        float bw = 900;
        float bh = 120;

        startButton = new Rect((w - bw) / 2f, h * 0.5f, bw, bh);
        quitButton = new Rect((w - bw) / 2f, h * 0.5f + 150, bw, bh);
    }

    private void drawButton(Rect r, Matrix4f ortho, Vector3f color) {
        shader.bind();

        Matrix4f mvp = new Matrix4f(ortho)
                .translate(r.x, r.y, 0)
                .scale(r.w, r.h, 1);

        shader.setUniformMat4f("uMVP", mvp);
        shader.setUniform3f("uColor", color);

        buttonMesh.render();
        shader.unbind();
    }


    private static class Rect {
        final float x, y, w, h;
        Rect(float x, float y, float w, float h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        boolean contains(float px, float py) {
            return px >= x && px <= x+w && py >= y && py <= y+h;
        }
    }
}
