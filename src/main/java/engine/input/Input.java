package engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.HashMap;
import java.util.Map;

public class Input {
    private final long windowHandle;
    private final Map<Action, Integer> bindings = new HashMap<>();
    private final Map<Integer, KeyState> keys = new HashMap<>();

    private GLFWCursorPosCallback cursorCallback;
    private GLFWKeyCallback keyCallback;

    private double mouseX, mouseY;
    private double lastMouseX, lastMouseY;
    private double deltaX, deltaY;
    private boolean firstMouse = true;

    public Input(long windowHandle) {
        this.windowHandle = windowHandle;

        keyCallback = GLFW.glfwSetKeyCallback(windowHandle, (win, key, scancode, action, mods) -> {
            if (!keys.containsKey(key)) return;
            if (action == GLFW.GLFW_PRESS) {
                keys.put(key, KeyState.PRESSED);
            }
            else if (action == GLFW.GLFW_RELEASE) {
                keys.put(key, KeyState.RELEASED);
            }
        });

        cursorCallback = GLFW.glfwSetCursorPosCallback(windowHandle, (win, x, y) -> {
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
            KeyState state = keys.get(key);

            if (state == KeyState.PRESSED) {
                keys.put(key, KeyState.DOWN);
            }
            else if (state == KeyState.RELEASED) {
                keys.put(key, KeyState.UP);
            }
        }
    }

    public void bind(Action action, int glfwKey) {
        bindings.put(action, glfwKey);
        registerKey(glfwKey);
    }

    public boolean isActionDown(Action action) {
        Integer key = bindings.get(action);
        return (key != null) && (isKeyDown(key));
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

    public void cleanup() {
        if (cursorCallback != null) { cursorCallback.free(); }
        if (keyCallback != null) { keyCallback.free(); }
    }
}
