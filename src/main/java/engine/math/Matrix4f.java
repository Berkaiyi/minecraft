package engine.math;

import engine.math.Vector3f;

import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryUtil;

public class Matrix4f {
    private final float[] m = new float[16];

    public Matrix4f() {
        identity();
    }

    public static Matrix4f perspective(float fov, float aspect, float near, float far) {
        Matrix4f m = new Matrix4f();
        float tan = (float) Math.tan(Math.toRadians(fov / 2f));

        m.m[0] = 1f / (aspect * tan);
        m.m[5] = 1f / tan;
        m.m[10] = -(far + near) / (far - near);
        m.m[11] = -1f;
        m.m[14] = -(2f * far * near) / (far - near);
        m.m[15] = 0f;

        return m;
    }

    public static Matrix4f lookAt(Vector3f pos, Vector3f forward, Vector3f up) {
        Vector3f f = forward.normalize();

        float sx = f.y * up.z - f.z * up.y;
        float sy = f.z * up.x - f.x * up.z;
        float sz = f.x * up.y - f.y * up.x;
        Vector3f s = new engine.math.Vector3f(sx, sy, sz).normalize();

        float ux = s.y * f.z - s.z * f.y;
        float uy = s.z * f.x - s.x * f.z;
        float uz = s.x * f.y - s.y * f.x;
        Vector3f u = new engine.math.Vector3f(ux, uy, uz);

        Matrix4f m = new Matrix4f();
        m.identity();

        // column-major
        m.m[0] = s.x; m.m[4] = s.y; m.m[8]  = s.z;
        m.m[1] = u.x; m.m[5] = u.y; m.m[9]  = u.z;
        m.m[2] = -f.x; m.m[6] = -f.y; m.m[10] = -f.z;

        // translation
        m.m[12] = -(s.x * pos.x + s.y * pos.y + s.z * pos.z);
        m.m[13] = -(u.x * pos.x + u.y * pos.y + u.z * pos.z);
        m.m[14] =  (f.x * pos.x + f.y * pos.y + f.z * pos.z);

        return m;
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

    public static Matrix4f rotationX(float angleRad) {
        Matrix4f m = new Matrix4f();

        float cos = (float) Math.cos(angleRad);
        float sin = (float) Math.sin(angleRad);

        m.m[5] = cos;
        m.m[6] = sin;
        m.m[9] = -sin;
        m.m[10] = cos;

        return m;
    }

    public static Matrix4f rotationY(float angleRad) {
        Matrix4f m = new Matrix4f();

        float cos = (float) Math.cos(angleRad);
        float sin = (float) Math.sin(angleRad);

        m.m[0] = cos;
        m.m[2] = -sin;
        m.m[8] = sin;
        m.m[10] = cos;

        return m;
    }

    public Matrix4f mul(Matrix4f o) {
        Matrix4f r = new Matrix4f();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                r.m[col * 4 + row] =
                        m[0 * 4 + row] * o.m[col * 4 + 0] +
                        m[1 * 4 + row] * o.m[col * 4 + 1] +
                        m[2 * 4 + row] * o.m[col * 4 + 2] +
                        m[3 * 4 + row] * o.m[col * 4 + 3];
            }
        }

        return r;
    }

    public Matrix4f rotateX(float angleRad) {
        float cos = (float) Math.cos(angleRad);
        float sin = (float) Math.sin(angleRad);

        float m5 = m[5] * cos + m[9] * sin;
        float m6 = m[6] * cos + m[10] * sin;
        float m9 = m[9] * cos - m[5] * sin;
        float m10 = m[10] * cos - m[6] * sin;

        m[5] = m5;
        m[6] = m6;
        m[9] = m9;
        m[10] = m10;

        return this;
    }

    public Matrix4f rotateY(float angleRad) {
        float cos = (float) Math.cos(angleRad);
        float sin = (float) Math.sin(angleRad);

        float m0 = m[0] * cos + m[8] * sin;
        float m2 = m[2] * cos + m[10] * sin;
        float m8 = m[8] * cos - m[0] * sin;
        float m10 = m[10] * cos - m[2] * sin;

        m[0] = m0;
        m[2] = m2;
        m[8] = m8;
        m[10] = m10;

        return this;
    }

    public FloatBuffer toBuffer() {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
        buffer.put(m).flip();
        return buffer;
    }
}
