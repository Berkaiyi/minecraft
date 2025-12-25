package engine.render;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private final int vaoId;
    private final int vboId;
    private final int eboId;
    private final int indexCount;

    private static final int FLOAT_SIZE = 4;
    private static final int STRIDE = 8 * FLOAT_SIZE;

    public Mesh(float[] vertices, int[] indices) {
        indexCount = indices.length;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        FloatBuffer vbuf = MemoryUtil.memAllocFloat(vertices.length);
        vbuf.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vbuf, GL_STATIC_DRAW);
        MemoryUtil.memFree(vbuf);


        glVertexAttribPointer(0, 3, GL_FLOAT, false, STRIDE, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, STRIDE, 3 * FLOAT_SIZE);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, STRIDE, 6 * FLOAT_SIZE);
        glEnableVertexAttribArray(2);


        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);

        IntBuffer ibuf = MemoryUtil.memAllocInt(indices.length);
        ibuf.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibuf, GL_STATIC_DRAW);
        MemoryUtil.memFree(ibuf);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);
        glDeleteVertexArrays(vaoId);
    }
}
