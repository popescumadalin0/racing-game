package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.MovingLamp;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();

		// *********TERRAIN TEXTURE STUFF**********

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		// *****************************************

		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("flower")));
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
				new ModelTexture(loader.loadTexture("lamp")));
		TexturedModel cherry = new TexturedModel(OBJLoader.loadObjModel("cherry", loader), 
		    new ModelTexture(loader.loadTexture("cherry")));

		

		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTextureAtlas);

		
		
		
		TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("pine", loader),
				new ModelTexture(loader.loadTexture("pine")));
		bobble.getTexture().setHasTransparency(true);

		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(676452);
		for (int i = 0; i < 400; i++) {
			if (i % 3 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, 0.9f));
			}
			if (i % 1 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(bobble,random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, random.nextFloat() * 0.1f + 0.6f));
			}
			
			if (i % 5 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(cherry,random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, random.nextFloat() * 0.1f + 0.6f));
			}
		}

		Light light = new Light(new Vector3f(0, 10000, -7000), new Vector3f(1, 1, 1));
		List<Light> lights = new ArrayList<Light>();
	
		Light lampLight1 = new Light(new Vector3f(185, terrain.getHeightOfTerrain(185, -293), -293), new Vector3f(1, 0, 0)); // Roșu
		Light lampLight2 = new Light(new Vector3f(370, terrain.getHeightOfTerrain(370, -300), -300), new Vector3f(0, 0, 1)); // Albastru
		Light lampLight3 = new Light(new Vector3f(293, terrain.getHeightOfTerrain(293, -305), -305), new Vector3f(1, 1, 0)); // Galben
		Light lampLight4 = new Light(new Vector3f(100, terrain.getHeightOfTerrain(100, -150), -150), new Vector3f(0, 1, 0)); // Verde
		Light lampLight5 = new Light(new Vector3f(500, terrain.getHeightOfTerrain(500, -450), -450), new Vector3f(1, 0, 1)); // Mov
		Light lampLight6 = new Light(new Vector3f(600, terrain.getHeightOfTerrain(600, -550), -550), new Vector3f(1, 0.5f, 0)); // Portocaliu

		// Adăugarea lămpilor la listă
		lights.add(lampLight1);
		lights.add(lampLight2);
		lights.add(lampLight3);
		lights.add(lampLight4);
		lights.add(lampLight5);
		lights.add(lampLight6);
	
		
		// Lămpile care se mișcă aleatoriu
		List<Entity> movingLamps = new ArrayList<Entity>();
		movingLamps.add(new MovingLamp(lamp, new Vector3f(185, terrain.getHeightOfTerrain(185, -293), -293), 0.5f));
		movingLamps.add(new MovingLamp(lamp, new Vector3f(370, terrain.getHeightOfTerrain(370, -300), -300), 0.5f));
		movingLamps.add(new MovingLamp(lamp, new Vector3f(293, terrain.getHeightOfTerrain(293, -305), -305), 0.5f));
		movingLamps.add(new MovingLamp(lamp, new Vector3f(100, terrain.getHeightOfTerrain(100, -150), -150), 0.5f));
		movingLamps.add(new MovingLamp(lamp, new Vector3f(500, terrain.getHeightOfTerrain(500, -450), -450), 0.5f));
		movingLamps.add(new MovingLamp(lamp, new Vector3f(600, terrain.getHeightOfTerrain(600, -550), -550), 0.5f));

		// Adăugarea lămpilor în lista de entități
		entities.addAll(movingLamps);


		MasterRenderer renderer = new MasterRenderer();

		RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
				loader.loadTexture("playerTexture")));

		Player player = new Player(stanfordBunny, new Vector3f(100, 5, -150), 0, 180, 0, 0.6f);
		Camera camera = new Camera(player);
		
		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("health"),new Vector2f(-0.8f,0.9f), new Vector2f(0.2f,0.3f));
		//guiTextures.add(gui);
		GuiRenderer guiRenderer = new GuiRenderer(loader);

		while (!Display.isCloseRequested()) {
		    player.move(terrain);

		    for (Entity entity : entities) {
		        if (player.checkCollision(entity)) {
		            System.out.println("Coliziune detectată cu " + entity.getModel());

		            // Calculăm vectorul de împingere
		            Vector3f pushBack = new Vector3f();
		            Vector3f.sub(player.getPosition(), entity.getPosition(), pushBack);

		            // Verificăm dacă vectorul de coliziune nu are lungimea zero
		            if (pushBack.length() != 0) {
		                pushBack.normalise();

		                // Mărim forța de împingere pentru a avea efect
		                player.increasePosition(pushBack.x * 2f, pushBack.y * 1.5f, pushBack.z * 1.5f);
		            }
		        }
		    }


		    camera.move();
		    renderer.processEntity(player);
		    renderer.processTerrain(terrain);

		    for (Entity entity : entities) {
		        renderer.processEntity(entity);
		    }
		    
		    for (Entity movingLamp : movingLamps) {
		        if (movingLamp instanceof MovingLamp) {
		            ((MovingLamp) movingLamp).update();  // Mișcarea aleatorie
		        }
		        renderer.processEntity(movingLamp);
		    }



		    renderer.render(lights, camera);
		    guiRenderer.render(guiTextures);
		    DisplayManager.updateDisplay();
		}

		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
