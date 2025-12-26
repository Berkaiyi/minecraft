package engine.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL20.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {
    private final int programId;

    public ShaderProgram(String vertexPath, String fragmentPath) {
        String vertexSrc = loadResource(vertexPath);
        String fragmentSrc = loadResource(fragmentPath);

        int vertexId = createShader(vertexSrc, GL_VERTEX_SHADER);
        int fragmentId = createShader(fragmentSrc, GL_FRAGMENT_SHADER);

        programId = glCreateProgram();
        glAttachShader(programId, vertexId);
        glAttachShader(programId, fragmentId);
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Shader link error: " + glGetProgramInfoLog(programId));
        }

        glDetachShader(programId, vertexId);
        glDetachShader(programId, fragmentId);
        glDeleteShader(vertexId);
        glDeleteShader(fragmentId);
    }

    public void setUniformMat4f(String name, Matrix4f mat) {
        int location = glGetUniformLocation(programId, name);
        if (location < 0) {
            throw new RuntimeException("Uniform not found" + name);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(location, false, mat.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform1i(String name, int value) {
        int location = glGetUniformLocation(programId, name);
        if (location < 0) {
            throw new RuntimeException("Uniform not found" + name);
        }
        glUniform1i(location, value);
    }

    public void setUniform3f(String name, Vector3f value) {
        int location = glGetUniformLocation(programId, name);
        if (location < 0) {
            throw new RuntimeException("Uniform not found" + name);
        }
        glUniform3f(location, value.x, value.y, value.z);
    }

    private static String loadResource(String path) {
        StringBuilder result = new StringBuilder();

        try (InputStream in = ShaderProgram.class.getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
            ) {
            if (in == null) {
                throw new RuntimeException("Shader not found: " + path);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append('\n');
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load shader: " + path, e);
        }

        return result.toString();
    }

    private int createShader(String src, int type) {
        int id = glCreateShader(type);
        glShaderSource(id, src);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Shader compile error: " + glGetShaderInfoLog(id));
        }
        return id;
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        glDeleteProgram(programId);
    }
}
