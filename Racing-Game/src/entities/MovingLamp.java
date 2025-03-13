package entities;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class MovingLamp extends Entity {
    private float speed;
    private Random random;

    public MovingLamp(TexturedModel model, Vector3f position, float speed) {
        super(model, position, 0, 0, 0, 1); // 0,0,0 pentru rotație, 1 pentru scalare
        this.speed = speed;
        this.random = new Random();
    }

    public void update() {
        // Mișcare aleatorie pe axele X și Z
        float randomX = (random.nextFloat() - 0.5f) * speed;
        float randomZ = (random.nextFloat() - 0.5f) * speed;

        // Actualizează poziția lămpii
        this.increasePosition(randomX, 0, randomZ);
    }
}

