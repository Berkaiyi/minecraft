package engine.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window;
    private final String title;
    private final int width, height;
    private GLFWFramebufferSizeCallback fbCallback;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("GLFW init failed");
        }

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);

        window = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0) {
            throw new RuntimeException("Couldn't create window");
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer fbW = stack.mallocInt(1);
            IntBuffer fbH = stack.mallocInt(1);
            glfwGetFramebufferSize(window, fbW, fbH);
            glViewport(0, 0, fbW.get(0), fbH.get(0));
        }
        fbCallback = glfwSetFramebufferSizeCallback(window, (win, fbWidth, fbHeight) -> {
            glViewport(0, 0, fbWidth, fbHeight);
        });
    }

    public long getHandle() {
        return window;
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    public void clear() {
        GL11.glClearColor(0f, 0f, 0f, 0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(window);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
    }

    public void close() {
        GLFW.glfwSetWindowShouldClose(window, true);
    }

    public void destroy() {
        if (fbCallback != null) { fbCallback.free(); }
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}
