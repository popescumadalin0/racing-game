package entities;

import org.lwjgl.util.vector.Vector3f;

public class CollisionDetector {
    
    public boolean checkCollision(Entity entity1, Entity entity2) {
        // Obține pozițiile și scările entităților
        Vector3f position1 = entity1.getPosition();
        Vector3f position2 = entity2.getPosition();
        float scale1 = entity1.getScale();
        float scale2 = entity2.getScale();
        
        // Calcularea distanței dintre cele două entități
        float distance = Vector3f.sub(position1, position2, null).length();
        
        // Aproximarea coliziunii cu sfera de coliziune
        float combinedRadius = scale1 + scale2;
        
        // Verifică dacă distanța dintre ele este mai mică decât raza combinată
        return distance < combinedRadius;
    }
}
