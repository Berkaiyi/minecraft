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

    public Mesh(float[] positions, int posSize, int[] indices) {
        indexCount = indices.length;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        FloatBuffer vbuf = MemoryUtil.memAllocFloat(positions.length);
        vbuf.put(positions).flip();
        glBufferData(GL_ARRAY_BUFFER, vbuf, GL_STATIC_DRAW);
        MemoryUtil.memFree(vbuf);

        glVertexAttribPointer(0, posSize, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);


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
