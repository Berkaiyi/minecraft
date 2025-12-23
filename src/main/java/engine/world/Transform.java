package engine.world;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    private final Vector3f position;

    public Transform(float x, float y, float z) {
        position = new Vector3f(x, y, z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f toModelMatrix() {
        return new Matrix4f().identity().translate(position.x, position.y, position.z);
    }
}
