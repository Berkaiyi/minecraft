package engine.core;

import engine.input.Action;
import engine.screen.Screen;
import engine.window.Window;
import engine.input.Input;
import game.screens.GameScreen;
import game.screens.MainMenuScreen;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    private static final int TARGET_UPS = 60;
    private static final double TIME_PER_UPDATE = 1.0 / TARGET_UPS;

    private int fps;
    private int ups;

    private Window window;
    private Timer timer;
    private Input input;

    private Screen currentScreen;

    public void run() {
        try {
            init();
            setScreen(new GameScreen(this));

            loop();
        }
        catch (Throwable t) {
            System.err.println("=== ENGINE CRASH ===");
            t.printStackTrace();
        }
        finally {
            cleanup();
        }
    }

    public void setScreen(Screen next) {
        if (currentScreen != null) { currentScreen.cleanup(); }
        currentScreen = next;
        currentScreen.init();
    }

    public Window getWindow() { return window; }

    private void init() {
        window = new Window("LWJGL Engine", 1920, 1080, true);
        window.init();

        timer = new Timer();
        input = new Input(window.getHandle());

        input.bind(Action.EXIT, GLFW_KEY_ESCAPE);
        input.bind(Action.MOVE_FORWARD, GLFW_KEY_W);
        input.bind(Action.MOVE_LEFT, GLFW_KEY_A);
        input.bind(Action.MOVE_BACKWARD, GLFW_KEY_S);
        input.bind(Action.MOVE_RIGHT, GLFW_KEY_D);
        input.bind(Action.MOVE_UP, GLFW_KEY_SPACE);
        input.bind(Action.MOVE_DOWN, GLFW_KEY_LEFT_SHIFT);
    }

    private void loop() {
        double accumulator = 0.0;

        while (!window.shouldClose()) {
            double delta = timer.getDelta();
            accumulator += delta;

            window.pollEvents();

            // INPUT -----------
            if (input.isActionDown(Action.EXIT)) {
                window.close();
            }
            // -----------

            input.update();

            while (accumulator >= TIME_PER_UPDATE) {
                if (currentScreen != null) { currentScreen.update(TIME_PER_UPDATE, input); }
                ups++;
                accumulator -= TIME_PER_UPDATE;
            }

            window.clear();
            if (currentScreen != null) { currentScreen.render(input); }
            window.swapBuffers();
            fps++;

            if (timer.hasSecondPassed(delta)) {
                System.out.println("FPS: " + fps + " | UPS: " + ups);
                fps = 0;
                ups = 0;
            }
        }
    }

    private void cleanup() {
        if (currentScreen != null) { currentScreen.cleanup(); }
        input.cleanup();
        window.destroy();
    }
}
