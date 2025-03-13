package entities;

import org.lwjgl.util.vector.Vector3f;

public class BoundingBox {
    private Vector3f min;
    private Vector3f max;

    public BoundingBox(Vector3f position, float size) {
        this.min = new Vector3f(position.x - size / 2, position.y, position.z - size / 2);
        this.max = new Vector3f(position.x + size / 2, position.y + size, position.z + size / 2);
    }

    public boolean intersects(BoundingBox other) {
        return (this.min.x <= other.max.x && this.max.x >= other.min.x) &&
               (this.min.y <= other.max.y && this.max.y >= other.min.y) &&
               (this.min.z <= other.max.z && this.max.z >= other.min.z);
    }

    public void updatePosition(Vector3f newPosition, float size) {
        this.min.set(newPosition.x - size / 2, newPosition.y, newPosition.z - size / 2);
        this.max.set(newPosition.x + size / 2, newPosition.y + size, newPosition.z + size / 2);
    }
}
