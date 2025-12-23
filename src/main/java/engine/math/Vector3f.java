package engine.math;

public class Vector3f {
    public float x, y, z;

    public Vector3f() {
        this(0, 0, 0);
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f add(Vector3f o) {
        return new Vector3f(x + o.x, y + o.y, z + o.z);
    }

    public Vector3f sub(Vector3f o) {
        return new Vector3f(x - o.x, y - o.y, z - o.z);
    }
}
