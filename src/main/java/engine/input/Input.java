package engine.input;

import org.lwjgl.glfw.GLFW;
import java.util.HashMap;
import java.util.Map;

public class Input {
    private final long windowHandle;
    private final Map<Integer, KeyState> keys = new HashMap<>();

    private double mouseX, mouseY;
    private double lastMouseX, lastMouseY;
    private double deltaX, deltaY;
    private boolean firstMouse = true;

    public Input(long windowHandle) {
        this.windowHandle = windowHandle;

        GLFW.glfwSetCursorPosCallback(windowHandle, (win, x, y) -> {
            mouseX = x;
            mouseY = y;
            if (firstMouse) {
                lastMouseX = x;
                lastMouseY = y;
                firstMouse = false;
            }
            deltaX = mouseX - lastMouseX;
            deltaY = mouseY - lastMouseY;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
        });
    }

    public void update() {
        for (int key : keys.keySet()) {
            boolean isDown = GLFW.glfwGetKey(windowHandle, key) == GLFW.GLFW_PRESS;
            KeyState state = keys.get(key);

            if (isDown) {
                keys.put(key, state == KeyState.UP ? KeyState.PRESSED : KeyState.DOWN);
            }
            else {
                keys.put(key, state == KeyState.DOWN ? KeyState.RELEASED : KeyState.UP);
            }
        }
    }

    public void registerKey(int key) {
        keys.putIfAbsent(key, KeyState.UP);
    }

    public boolean isKeyPressed(int key) {
        KeyState state = keys.get(key);
        if (state == null) {
            throw new IllegalStateException("Key is not registered: " + key);
        }
        return state == KeyState.PRESSED;
    }

    public boolean isKeyDown(int key) {
        return keys.get(key) == KeyState.DOWN || keys.get(key) == KeyState.PRESSED;
    }

    public boolean isKeyReleased(int key) {
        return keys.get(key) == KeyState.RELEASED;
    }

    public double consumeMouseDeltaX() {
        double dx = deltaX;
        deltaX = 0;
        return dx;
    }

    public double consumeMouseDeltaY() {
        double dy = deltaY;
        deltaY = 0;
        return dy;
    }

    public void resetMouse() {
        firstMouse = true;
        deltaX = 0;
        deltaY = 0;
    }
}
