package engine.input;

import engine.util.Log;
import engine.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Input {
    private final Window window;
    private final long windowHandle;
    private final Map<Action, Integer> bindings = new HashMap<>();
    private final Map<Integer, KeyState> keys = new HashMap<>();
    private final Map<Integer, KeyState> mouseButtons = new HashMap<>();
    private final Set<Integer> mousePressedEvents = new HashSet<>(); // TODO: press+release can happen simultaneously

    private final GLFWCursorPosCallback cursorCallback;
    private final GLFWKeyCallback keyCallback;
    private final GLFWMouseButtonCallback mouseButtonCallback;

    private double mouseX, mouseY;
    private double lastMouseX, lastMouseY;
    private double deltaX, deltaY;
    private boolean firstMouse = true;

    public Input(Window window) {
        this.window = window;
        this.windowHandle = window.getHandle();

        keyCallback = GLFW.glfwSetKeyCallback(windowHandle, (win, key, scancode, action, mods) -> {
            if (!keys.containsKey(key)) return;
            if (action == GLFW.GLFW_PRESS) { keys.put(key, KeyState.PRESSED); }
            else if (action == GLFW.GLFW_RELEASE) { keys.put(key, KeyState.RELEASED); }
        });

        mouseButtonCallback = GLFW.glfwSetMouseButtonCallback(windowHandle, (win, button, action, mods) -> {
            if (!mouseButtons.containsKey(button)) return;
            if (action == GLFW.GLFW_PRESS) {
                mouseButtons.put(button, KeyState.PRESSED);
                mousePressedEvents.add(button);
            }
            else if (action == GLFW.GLFW_RELEASE) { mouseButtons.put(button, KeyState.RELEASED); }
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
            if (state == KeyState.PRESSED) { keys.put(key, KeyState.DOWN); }
            else if (state == KeyState.RELEASED) { keys.put(key, KeyState.UP); }
        }

        for (int button : mouseButtons.keySet()) {
            KeyState state = mouseButtons.get(button);
            if (state == KeyState.PRESSED) { mouseButtons.put(button, KeyState.DOWN); }
            else if (state == KeyState.RELEASED) { mouseButtons.put(button, KeyState.UP); }
        }

        mousePressedEvents.clear();
    }

    public void bindKey(Action action, int glfwKey) {
        bindings.put(action, glfwKey);
        registerKey(glfwKey);
        Log.debug("Input", "bindKey: %s -> %d", action, glfwKey);
    }
    public void bindButton(Action action, int glfwButton) {
        bindings.put(action, glfwButton);
        registerMouseButton(glfwButton);
        Log.debug("Input", "bindButton: %s -> %d", action, glfwButton);
    }

    public boolean isActionPressed(Action action) {
        Integer key = bindings.get(action);
        if (key == null) {
            Log.warn("Input", "Action not bound: %s", action);
            return false;
        }
        if (keys.containsKey(key)) { return isKeyPressed(key); }
        if (mouseButtons.containsKey(key)) { return isMousePressed(key); }
        return false;
    }

    public boolean isActionDown(Action action) {
        Integer key = bindings.get(action);
        if (key == null) {
            Log.warn("Input", "Action not bound: %s", action);
            return false;
        }
        if (keys.containsKey(key)) { return isKeyDown(key); }
        if (mouseButtons.containsKey(key)) { return isMouseDown(key); }
        return false;
    }

    public void registerKey(int key) { keys.putIfAbsent(key, KeyState.UP); }
    public void registerMouseButton(int button) { mouseButtons.putIfAbsent(button, KeyState.UP); }

    public double getMouseX() { return mouseX; }
    public double getMouseY() { return mouseY; }
    public double getMouseXFb() { return mouseX * window.getFbWidth() / window.getWidth(); }
    public double getMouseYFb() { return mouseY * window.getFbHeight() / window.getHeight(); }
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
        Log.debug("Input", "resetMouse()");
        firstMouse = true;
        deltaX = 0;
        deltaY = 0;
    }

    public void cleanup() {
        Log.info("Input", "cleanup callback");
        if (cursorCallback != null) { cursorCallback.free(); }
        if (keyCallback != null) { keyCallback.free(); }
        if (mouseButtonCallback != null) { mouseButtonCallback.free(); }
    }

    private boolean isKeyPressed(int key) {
        return keys.get(key) == KeyState.PRESSED;
    }
    private boolean isKeyDown(int key) {
        return keys.get(key) == KeyState.DOWN || keys.get(key) == KeyState.PRESSED;
    }
    private boolean isKeyReleased(int key) {
        return keys.get(key) == KeyState.RELEASED;
    }
    private boolean isMousePressed(int button) {
        return mouseButtons.get(button) == KeyState.PRESSED;
    }
    private boolean isMouseDown(int button) {
        return mouseButtons.get(button) == KeyState.DOWN || mouseButtons.get(button) == KeyState.PRESSED;
    }
    private boolean isMouseReleased(int button) {
        return mouseButtons.get(button) == KeyState.RELEASED;
    }
}
