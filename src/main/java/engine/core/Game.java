package engine.core;

import engine.window.Window;
import engine.input.Input;
import static org.lwjgl.glfw.GLFW.*;

public class Game {
    private static final int TARGET_FPS = 60;
    private static final double TIME_PER_UPDATE = 1.0 / TARGET_FPS;

    private final GameLogic logic;
    private Window window;
    private Timer timer;
    private Input input;

    public Game(GameLogic logic) {
        this.logic = logic;
    }

    public void run() {
        try {
            init();
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

    private void init() {
        window = new Window("LWJGL Engine", 1280, 720);
        window.init();

        timer = new Timer();

        input = new Input(window.getHandle());
        input.registerKey(GLFW_KEY_ESCAPE);


        logic.init();
    }

    private void loop() {
        double accumulator = 0.0;

        while (!window.shouldClose()) {
            double delta = timer.getDelta();
            accumulator += delta;

            window.pollEvents();
            input.update();

            if (input.isKeyPressed(GLFW_KEY_ESCAPE)) {
                window.close();
            }

            while (accumulator >= TIME_PER_UPDATE) {
                logic.update(TIME_PER_UPDATE, input);
                accumulator -= TIME_PER_UPDATE;
            }

            logic.render();
            window.clear();
            window.swapBuffers();
        }
    }

    private void cleanup() {
        logic.cleanup();
        window.destroy();
    }
}
