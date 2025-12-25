package game.screens;

import engine.core.Game;
import engine.input.Input;
import engine.render.Mesh;
import engine.render.Renderer;
import engine.render.ShaderProgram;
import engine.screen.Screen;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MainMenuScreen implements Screen {

    private final Game game;

    private Renderer renderer;
    private ShaderProgram shader;
    private Mesh quad;

    private Rect startBtn;
    private Rect quitBtn;

    public MainMenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void init() {
        game.getWindow().setCursorCaptured(false);

        renderer = new Renderer();
        shader = new ShaderProgram("/shaders/menu.vert", "/shaders/menu.frag");

        // Ein einfaches Quad in NDC bauen wir über MVP -> deshalb reicht Position
        // Wir benutzen trotzdem Mesh (pos/normal/uv/color Layout wäre overkill),
        // daher: Ich gehe davon aus, dass dein Mesh mindestens aPos location=0 nutzen kann.
        // Falls dein Mesh FIX 11-float layout erzwingt: sag Bescheid, dann liefere ich Quad-Daten im 11er Layout.
        //quad = MeshFactory.makeUnitQuad();

        int w = game.getWindow().getFbWidth();
        int h = game.getWindow().getFbHeight();

        float bw = 300;
        float bh = 70;

        startBtn = new Rect((w - bw) * 0.5f, (h * 0.45f), bw, bh);
        quitBtn  = new Rect((w - bw) * 0.5f, (h * 0.60f), bw, bh);
    }

    @Override
    public void update(double dt, Input input) {
        // Resize: Hitboxes neu berechnen
        if (game.getWindow().isResize()) {
            game.getWindow().setResize(false);
            int w = game.getWindow().getFbWidth();
            int h = game.getWindow().getFbHeight();
            float bw = 300, bh = 70;
            startBtn = new Rect((w - bw) * 0.5f, (h * 0.45f), bw, bh);
            quitBtn  = new Rect((w - bw) * 0.5f, (h * 0.60f), bw, bh);
        }

        if (input.isMousePressed(GLFW_MOUSE_BUTTON_LEFT)) {
            float mx = (float) input.getMouseX();
            float my = (float) input.getMouseY();

            if (startBtn.contains(mx, my)) {
                game.setScreen(new GameScreen(game));
            } else if (quitBtn.contains(mx, my)) {
                game.getWindow().close();
            }
        }
    }

    @Override
    public void render(Input input) {
        int w = game.getWindow().getFbWidth();
        int h = game.getWindow().getFbHeight();

        // Ortho in Pixeln: (0,0) oben links -> passt zu GLFW mouse coords
        Matrix4f ortho = new Matrix4f().ortho2D(0, w, h, 0);

        drawButton(startBtn, ortho, new Vector2f(0.2f, 0.8f)); // grün-ish
        drawButton(quitBtn, ortho, new Vector2f(0.8f, 0.2f)); // rot-ish
    }

    private void drawButton(Rect r, Matrix4f ortho, Vector2f colorRG) {
        shader.bind();

        // MVP = ortho * translate(x,y) * scale(w,h)
        Matrix4f mvp = new Matrix4f(ortho)
                .translate(r.x, r.y, 0)
                .scale(r.w, r.h, 1);

        //shader.setUniformMat4("uMVP", mvp);
        //shader.setUniform3f("uColor", colorRG.x, colorRG.y, 0.2f);

        quad.render();

        shader.unbind();
    }

    @Override
    public void cleanup() {
        if (quad != null) quad.cleanup();
        if (shader != null) shader.cleanup();
    }


    private static class Rect {
        final float x, y, w, h;
        Rect(float x, float y, float w, float h) { this.x=x; this.y=y; this.w=w; this.h=h; }
        boolean contains(float px, float py) {
            return px >= x && px <= x+w && py >= y && py <= y+h;
        }
    }
}
