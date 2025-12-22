package game;

import engine.core.GameLogic;
import engine.input.Input;

import static org.lwjgl.glfw.GLFW.*;

public class MyGame implements GameLogic {
    @Override
    public void init() {
        System.out.println("Game init");
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
        // Rendering
    }

    @Override
    public void cleanup() {
        System.out.println("Game cleanup");
    }
}
