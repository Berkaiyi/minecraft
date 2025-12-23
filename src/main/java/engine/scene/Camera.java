package engine.scene;

import engine.math.Matrix4f;
import engine.math.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f(0, 0, 2);

    private float yaw = -90f;
    private float pitch = 0f;

    private Vector3f forward = new Vector3f(0, 0, -1);
    private Vector3f right = new Vector3f(1, 0, 0);
    private final Vector3f up = new Vector3f(0, 1, 0);

    public Vector3f getPosition() { return position; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }

    public Vector3f getForward() { return forward; }
    public Vector3f getRight() { return right; }
    public Vector3f getUp() { return up; }

    public void addYawPitch(float deltaYaw, float deltaPitch) {
        yaw += deltaYaw;
        pitch += deltaPitch;

        if (pitch > 89f) { pitch = 89f; }
        if (pitch < -89f) { pitch = -89f; }

        recalcVectors();
    }

    public void move(Vector3f delta) {
        position = position.add(delta);
    }

    private void recalcVectors() {
        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        float fx = (float) (Math.cos(yawRad) * Math.cos(pitchRad));
        float fy = (float) (Math.sin(pitchRad));
        float fz = (float) (Math.sin(yawRad) * Math.cos(pitchRad));

        forward = new Vector3f(fx, fy, fz).normalize();

        float rx = forward.y * up.z - forward.z * up.y;
        float ry = forward.z * up.x - forward.x * up.z;
        float rz = forward.x * up.y - forward.y * up.x;
        right = new Vector3f(rx, ry, rz).normalize();
    }

    public Vector3f getForwardXZ() {
        return new Vector3f(forward.x, 0f, forward.z).normalize();
    }

    public Matrix4f getViewMatrix() {
        return Matrix4f.lookAt(position, forward, up);
    }
}
