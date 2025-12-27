package engine.window;

import engine.util.Log;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;

public class Window {
    private long window;
    private String title;
    private int width, height;
    private int fbWidth, fbHeight;
    private boolean resize, vSync;
    private GLFWFramebufferSizeCallback fbCallback;
    private GLFWWindowSizeCallback winCallback;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("GLFW init failed");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        boolean maximised = false;
        if (width == 0 || height == 0) {
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximised = true;
        }

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Couldn't create window");
        }
        Log.info("Window", "Created window handle=%d size=%dx%d", window, width, height);

        fbCallback = GLFW.glfwSetFramebufferSizeCallback(window, (win, fbWidth, fbHeight) -> {
            this.fbWidth = fbWidth;
            this.fbHeight = fbHeight;
            resize = true;
            Log.debug("Window", "Framebuffer callback: fb=%dx%d", fbWidth, fbHeight);
        });

        winCallback = GLFW.glfwSetWindowSizeCallback(window, (win, width, height) -> {
            this.width = width;
            this.height = height;
        });

        if (maximised) {
            GLFW.glfwMaximizeWindow(window);
        }
        else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            if (vidMode != null) {
                GLFW.glfwSetWindowPos(window,
                        (vidMode.width() - width) / 2,
                        (vidMode.height() - height) / 2);
            }
        }

        GLFW.glfwMakeContextCurrent(window);
        if (isVsync()) {
            GLFW.glfwSwapInterval(1);
        }
        Log.info("Window", "VSync=%s", vSync);
        GLFW.glfwShowWindow(window);

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glDepthFunc(GL_LESS);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            GLFW.glfwGetWindowSize(window, width, height);
            this.width = width.get(0);
            this.height = height.get(0);

            GLFW.glfwGetFramebufferSize(window, width, height);
            fbWidth = width.get(0);
            fbHeight = height.get(0);
            glViewport(0, 0, fbWidth, fbHeight);
            Log.info("Window", "Initial framebuffer=%dx%d", fbWidth, fbHeight);
        }
    }

    public int getFbWidth() { return fbWidth; }
    public int getFbHeight() { return fbHeight; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isResize() { return resize; }
    public void applyResize()
    {
        glViewport(0, 0, this.fbWidth, this.fbHeight);
        resize = false;
        Log.debug("Window", "applyResize(): viewport set to %dx%d", fbWidth, fbHeight);
    }
    public boolean isVsync() { return vSync; }
    public void setVsync(boolean vSync) { this.vSync = vSync; }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
        this.title = title;
    }
    public void setCursorCaptured(boolean captured) {
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
                captured ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
        Log.debug("Window", "Cursor captured=%s", captured);
    }

    public long getHandle() { return window; }
    public void pollEvents() { GLFW.glfwPollEvents(); }
    public void clear() {
        GL11.glClearColor(0f, 0f, 0f, 0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    public void swapBuffers() { GLFW.glfwSwapBuffers(window); }
    public boolean shouldClose() { return GLFW.glfwWindowShouldClose(window); }
    public void close() { GLFW.glfwSetWindowShouldClose(window, true); }
    public void destroy() {
        if (winCallback != null) { winCallback.free(); }
        if (fbCallback != null) { fbCallback.free(); }
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}
