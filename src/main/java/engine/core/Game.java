package engine.core;

import engine.input.Action;
import engine.screen.Screen;
import engine.window.Window;
import engine.input.Input;
import game.screens.GameScreen;

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
        window = new Window("LWJGL Engine", 1280, 720, true);
        window.init();

        timer = new Timer();
        input = new Input(window);

        input.bindKey(Action.EXIT, GLFW_KEY_ESCAPE);
        input.bindKey(Action.MOVE_FORWARD, GLFW_KEY_W);
        input.bindKey(Action.MOVE_LEFT, GLFW_KEY_A);
        input.bindKey(Action.MOVE_BACKWARD, GLFW_KEY_S);
        input.bindKey(Action.MOVE_RIGHT, GLFW_KEY_D);
        input.bindKey(Action.MOVE_UP, GLFW_KEY_SPACE);
        input.bindKey(Action.MOVE_DOWN, GLFW_KEY_LEFT_SHIFT);
        input.bindButton(Action.LEFT_CLICK, GLFW_MOUSE_BUTTON_LEFT);
    }

    private void loop() {
        double accumulator = 0.0;

        while (!window.shouldClose()) {
            double delta = timer.getDelta();
            accumulator += delta;

            window.pollEvents();

            if (currentScreen != null) { currentScreen.handleInput(input); }
            window.applyResize();

            while (accumulator >= TIME_PER_UPDATE) {
                if (currentScreen != null) { currentScreen.update(TIME_PER_UPDATE, input); }
                ups++;
                accumulator -= TIME_PER_UPDATE;
            }

            input.update();
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
