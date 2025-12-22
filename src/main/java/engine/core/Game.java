package engine.core;

import engine.window.Window;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class Game {
    private static final int TARGET_FPS = 60;
    private static final double TIME_PER_UPDATE = 1.0 / TARGET_FPS;

    private final GameLogic logic;
    private Window window;
    private Timer timer;

    public Game(GameLogic logic) {
        this.logic = logic;
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        window = new Window("LWJGL Engine", 1280, 720);
        window.init();

        timer = new Timer();
        logic.init();
    }

    private void loop() {
        double accumulator = 0.0;

        while (!window.shouldClose()) {
            double delta = timer.getDelta();
            accumulator += delta;

            window.pollEvents();

            if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
                window.close();
            }

            while (accumulator >= TIME_PER_UPDATE) {
                logic.update(TIME_PER_UPDATE);
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
