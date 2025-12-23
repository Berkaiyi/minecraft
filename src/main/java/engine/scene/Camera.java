package engine.scene;

import engine.math.Matrix4f;
import engine.math.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f(0, 0, 2);

    public Matrix4f getViewMatrix() {
        return Matrix4f.lookAt(position, new Vector3f(0, 0, 0));
    }

    public Vector3f getPosition() {
        return position;
    }
}
