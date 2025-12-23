package engine.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private final Vector3f position =   new Vector3f(0, 1.6f, 3);
    private final Vector3f moveDir =    new Vector3f();
    private final Vector3f temp =       new Vector3f();

    private float yaw = -90f;
    private float pitch = 0f;

    private final Vector3f forward =    new Vector3f(0, 0, -1);
    private final Vector3f right =      new Vector3f(1, 0, 0);
    private final Vector3f up =         new Vector3f(0, 1, 0);
    private final Vector3f forwardXZ =  new Vector3f();
    private final Vector3f rightXZ =    new Vector3f();

    public void move(float amount) {
        if (moveDir.lengthSquared() > 0) {
            temp.set(moveDir).normalize().mul(amount);
            position.add(temp);
        }
        moveDir.zero();
    }

    public void addMoveForward() {
        moveDir.add(forwardXZ);
    }
    public void addMoveBackward() {
        moveDir.sub(forwardXZ);
    }
    public void addMoveRight() {
        moveDir.add(rightXZ);
    }
    public void addMoveLeft() {
        moveDir.sub(rightXZ);
    }
    public void addMoveUp() {
        moveDir.add(up);
    }
    public void addMoveDown() {
        moveDir.sub(up);
    }

    public void addYawPitch(float deltaYaw, float deltaPitch) {
        yaw += deltaYaw;
        pitch += deltaPitch;

        pitch = Math.max(-89f, Math.min(89f, pitch));
        recalcVectors();
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, new Vector3f(position).add(forward), up);
    }

    private void recalcVectors() {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        forward.set(
            (float) (Math.cos(yawRad) * Math.cos(pitchRad)),
            (float) (Math.sin(pitchRad)),
            (float) (Math.sin(yawRad) * Math.cos(pitchRad))
        ).normalize();

        // Movement forward (Yaw-only)
        forwardXZ.set(
            (float) Math.cos(yawRad),
            0f,
            (float) Math.sin(yawRad)
        ).normalize();

        // Movement right (Yaw-only)
        rightXZ.set(
            -forwardXZ.z,
            0f,
            forwardXZ.x
        ).normalize();

        forward.cross(up, right).normalize();
    }
}
