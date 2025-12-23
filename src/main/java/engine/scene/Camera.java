package engine.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private final Vector3f position = new Vector3f(0, 1.6f, 3);

    private float yaw = -90f;
    private float pitch = 0f;

    private final Vector3f forward =    new Vector3f(0, 0, -1);
    private final Vector3f right =      new Vector3f(1, 0, 0);
    private final Vector3f up =         new Vector3f(0, 1, 0);


    public Vector3f getPosition()   { return position; }
    public float getYaw()           { return yaw; }
    public float getPitch()         { return pitch; }

    public Vector3f getForward()    { return new Vector3f(forward); }
    public Vector3f getRight()      { return new Vector3f(right); }
    public Vector3f getUp()         { return new Vector3f(up); }

    public void addYawPitch(float deltaYaw, float deltaPitch) {
        yaw += deltaYaw;
        pitch += deltaPitch;

        pitch = Math.max(-89f, Math.min(89f, pitch));
        recalcVectors();
    }

    private void recalcVectors() {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        forward.set(
            (float) (Math.cos(yawRad) * Math.cos(pitchRad)),
            (float) (Math.sin(pitchRad)),
            (float) (Math.sin(yawRad) * Math.cos(pitchRad))
        ).normalize();

        forward.cross(up, right).normalize();
    }

    public Vector3f getForwardXZ() {
        return new Vector3f(forward.x, 0f, forward.z).normalize();
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, new Vector3f(position).add(forward), up);
    }
}
