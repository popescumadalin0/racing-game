package engineTester;

import org.lwjgl.util.vector.Vector3f;

public class CircularPathFollower {
    private static final float CENTER_X = 344.63035f;
    private static final float CENTER_Y = 41.49656f;
    private static final float CENTER_Z = -362.9787f;
    private static final float RADIUS = 82.35f;
    private float angle = 0;
    private float angularSpeed = 40.5f;

    public Vector3f getNextPosition(float deltaTime) {
        angle += angularSpeed * deltaTime;

        float theta = (float) Math.toRadians(angle);

        float newX = CENTER_X + RADIUS * (float) Math.cos(theta);
        float newZ = CENTER_Z + RADIUS * (float) Math.sin(theta);

        return new Vector3f(newX, CENTER_Y, newZ);
    }
}