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

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		// *****************************************

		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
				new ModelTexture(loader.loadTexture("lamp")));

		TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("pine", loader),
				new ModelTexture(loader.loadTexture("pine")));
		bobble.getTexture().setHasTransparency(true);

		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(676452);
		for (int i = 0; i < 200; i++) {

			if (i % 1 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z) - 3;
				entities.add(new Entity(bobble, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, random.nextFloat() * 0.1f + 0.6f));
			}

		}

		List<Light> lights = new ArrayList<Light>();

		Light lampLight1 = new Light(new Vector3f(185, terrain.getHeightOfTerrain(185, -293)+4, -293),
				new Vector3f(0, 0, 1)); 
		Light lampLight2 = new Light(new Vector3f(370, terrain.getHeightOfTerrain(370, -300)+4, -300),
				new Vector3f(1, 0, 0));
		Light lampLight3 = new Light(new Vector3f(293, terrain.getHeightOfTerrain(293, -305)+4, -305),
				new Vector3f(0, 1, 0));
		Light lampLight4 = new Light(new Vector3f(100, terrain.getHeightOfTerrain(100, -150)+4, -150),
				new Vector3f(1, 0, 1));
		Light lampLight5 = new Light(new Vector3f(500, terrain.getHeightOfTerrain(500, -450)+4, -450),
				new Vector3f(1, 1, 1));
		Light lampLight6 = new Light(new Vector3f(600, terrain.getHeightOfTerrain(600, -550)+4, -550),
				new Vector3f(0, 1, 1));

		lights.add(lampLight1);
		lights.add(lampLight2);
		lights.add(lampLight3);
		lights.add(lampLight4);
		lights.add(lampLight5);
		lights.add(lampLight6);

		List<Entity> movingLamps = new ArrayList<Entity>();
		movingLamps.add(new MovingLamp(lamp, new Vector3f(185, terrain.getHeightOfTerrain(185, -293)-1, -293), 0.5f));
		movingLamps.add(new MovingLamp(lamp, new Vector3f(370, terrain.getHeightOfTerrain(370, -300)-1, -300), 0.5f));
		movingLamps.add(new MovingLamp(lamp, new Vector3f(293, terrain.getHeightOfTerrain(293, -305)-1, -305), 0.5f));
		movingLamps.add(new MovingLamp(lamp, new Vector3f(100, terrain.getHeightOfTerrain(100, -150)-1, -150), 0.5f));
		movingLamps.add(new MovingLamp(lamp, new Vector3f(500, terrain.getHeightOfTerrain(500, -450)-1, -450), 0.5f));
		movingLamps.add(new MovingLamp(lamp, new Vector3f(600, terrain.getHeightOfTerrain(600, -550)-1, -550), 0.5f));

		entities.addAll(movingLamps);

		MasterRenderer renderer = new MasterRenderer();

		RawModel carModel = OBJLoader.loadObjModel("person", loader);
		TexturedModel car = new TexturedModel(carModel, new ModelTexture(loader.loadTexture("playerTexture")));

		Player player = new Player(car, new Vector3f(180, 5, -300), 0, 180, 0, 2.5f);
		Camera camera = new Camera(player);

		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.8f, 0.9f),
				new Vector2f(0.2f, 0.3f));
		GuiRenderer guiRenderer = new GuiRenderer(loader);

		while (!Display.isCloseRequested()) {
			player.move(terrain);

			for (Entity entity : entities) {
				if (player.checkCollision(entity)) {

					Vector3f pushBack = new Vector3f();
					Vector3f.sub(player.getPosition(), entity.getPosition(), pushBack);

					if (pushBack.length() != 0) {
						pushBack.normalise();

						player.increasePosition(pushBack.x * 2f, pushBack.y, pushBack.z * 1.5f);
					}
				}
			}

			camera.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);

			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}

			Entity movingLamp = movingLamps.getFirst();

			if (movingLamp instanceof MovingLamp) {
				((MovingLamp) movingLamp).update();
			}
			renderer.processEntity(movingLamp);

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
