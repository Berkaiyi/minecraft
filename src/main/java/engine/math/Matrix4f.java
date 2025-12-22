package engine.math;

import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryUtil;

public class Matrix4f {
    private final float[] m = new float[16];

    public Matrix4f() {
        identity();
    }

    public Matrix4f identity() {
        for (int i = 0; i < 16; i++) {
            m[i] = 0f;
        }
        m[0] = 1f;
        m[5] = 1f;
        m[10] = 1f;
        m[15] = 1f;
        return this;
    }

    public Matrix4f translate(float x, float y, float z) {
        m[12] += x;
        m[13] += y;
        m[14] += z;
        return this;
    }

    public Matrix4f scale(float s) {
        m[0] *= s;
        m[5] *= s;
        m[10] *= s;
        return this;
    }

    public FloatBuffer toBuffer() {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
        buffer.put(m).flip();
        return buffer;
    }
}
