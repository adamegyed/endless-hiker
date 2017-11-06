package com.egyed.adam.endlesshiker.game;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.engine.GameLogic;
import com.egyed.adam.endlesshiker.engine.MainWindow;
import com.egyed.adam.endlesshiker.engine.graphics.Camera;
import com.egyed.adam.endlesshiker.engine.graphics.HeightMapMesh;
import com.egyed.adam.endlesshiker.game.world.World;
import com.egyed.adam.endlesshiker.game.worldgen.HeightMapGenerator;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adam on 2/4/17.
 * Core game logic
 */
public class EHGame implements GameLogic {

  private final Renderer renderer;

  private World world;

  private Camera camera;

  private GameConsole console;

  // Increments are used to store the delta that should be applied to each next physics frame - for free camera
  private final Vector3f cameraInc;
  private final Vector3f cameraRotInc;

  // Follow Camera Increments
  private final Vector3f followCameraInc;
  // x is aap change, y is pitch change, and z is zoom change


  private final Vector2f playerMovInc;
  private float playerRotInc;
  private boolean jumpRequested;
  private boolean freeCamera;
  private boolean freeCameraMod = false;

  // Modifiers are used to not repeatedly do each action when the key is held
  private boolean wireframe = false;
  private boolean wireframeMod = false;
  private boolean culling = true;
  private boolean cullingMod = false;

  private boolean slowedCamera;
  private boolean slowedCameraMod;

  private boolean isNoiseRunning = false;
  private boolean isNoiseComplete = false;
  private boolean noiseMod = false;

  public final Object worldGeneratorLockObject = new Object();

  private Thread worldGeneratorThread = null;

  private float[][] heights = null;

  private boolean collisions = false;
  private boolean collisionsMod = false;

  // Distances each should move per step
  private static final float CAMERA_POS_STEP = 0.5f;
  private static final float CAMERA_ROT_STEP = 2.0f;
  private static final float SHIFT_STEP = 3f;
  private static final float MOVEMENT_STEP = 0.2f;
  private static final float TURN_STEP = 2.0f;

  // Follow Camera Constants
  private static final float CAMERA_AAP_STEP = 1.2f;
  private static final float CAMERA_DISTANCE_STEP = 0.1f;
  private static final float CAMERA_PITCH_STEP = 0.75f;

  // Camera defaults
  private static final Vector3f CAMERA_DEFAULT_POS = new Vector3f(0,7f, -7f);
  private static final Vector3f CAMERA_DEFAULT_ROT = new Vector3f(45f,180f,0);



  public EHGame() {
    renderer = new Renderer();
    camera = new Camera();

    world = new World();
    jumpRequested = false;

    cameraInc = new Vector3f(0,0,0);
    cameraRotInc = new Vector3f(0,0,0);
    playerMovInc = new Vector2f(0,0);
    followCameraInc = new Vector3f(0,0,0);
    playerRotInc = 0;
    freeCamera = false;
    console = new GameConsole();
  }


  @Override
  public void init(MainWindow mainWindow) throws Exception {
    renderer.init(mainWindow);

    resetCamera();

    //glClearColor(0.3f, 0.5f, 0.65f, 0.7f);

    world.init();

    console.log("Endless Hiker started");

    //startWorldGen();

  }

  @Override
  public void input(MainWindow mainWindow) {

    cameraInc.set(0, 0, 0);
    cameraRotInc.set(0,0,0);
    playerMovInc.set(0,0);
    followCameraInc.set(0,0,0);
    playerRotInc = 0;


    if (mainWindow.isKeyPressed(GLFW_KEY_Q)) {
      // Rotate player left - for now
      playerRotInc = -TURN_STEP;
    } else if (mainWindow.isKeyPressed(GLFW_KEY_E)) {
      // Rotate player right
      playerRotInc = TURN_STEP;
    }
    if (mainWindow.isKeyPressed(GLFW_KEY_W)) {
      playerMovInc.y = MOVEMENT_STEP;
    } else if (mainWindow.isKeyPressed(GLFW_KEY_S)) {
      playerMovInc.y = -MOVEMENT_STEP;
    }
    if (mainWindow.isKeyPressed(GLFW_KEY_A)) {
      playerMovInc.x = MOVEMENT_STEP;
    } else if (mainWindow.isKeyPressed(GLFW_KEY_D)) {
      playerMovInc.x = -MOVEMENT_STEP;
    }

    jumpRequested = mainWindow.isKeyPressed(GLFW_KEY_SPACE);

    if (freeCamera) { // Free Camera Controls
      if (mainWindow.isKeyPressed(GLFW_KEY_UP)) {
        cameraInc.z = -1;
      } else if (mainWindow.isKeyPressed(GLFW_KEY_DOWN)) {
        cameraInc.z = 1;
      }
      if (mainWindow.isKeyPressed(GLFW_KEY_LEFT)) {
        cameraInc.x = -1;
      } else if (mainWindow.isKeyPressed(GLFW_KEY_RIGHT)) {
        cameraInc.x = 1;
      }
      if (mainWindow.isKeyPressed(GLFW_KEY_X)) {
        cameraInc.y = -1;
      } else if (mainWindow.isKeyPressed(GLFW_KEY_Z)) {
        cameraInc.y = 1;
      }
      if (mainWindow.isKeyPressed(GLFW_KEY_N)) {
        cameraRotInc.y = 1;
      } else if (mainWindow.isKeyPressed(GLFW_KEY_B)) {
        cameraRotInc.y = -1;
      }
      if (mainWindow.isKeyPressed(GLFW_KEY_F)) {
        cameraRotInc.x = -1;
      } else if (mainWindow.isKeyPressed(GLFW_KEY_V)) {
        cameraRotInc.x = 1;
      }
    }
    else { // Follow Camera Mode
      if (mainWindow.isKeyPressed(GLFW_KEY_UP)) {
        followCameraInc.y = 1;
      } else if (mainWindow.isKeyPressed(GLFW_KEY_DOWN)) {
        followCameraInc.y = -1;
      }
      if (mainWindow.isKeyPressed(GLFW_KEY_LEFT)) {
        followCameraInc.x = 1;
      } else if (mainWindow.isKeyPressed(GLFW_KEY_RIGHT)) {
        followCameraInc.x = -1;
      }
      if (mainWindow.isKeyPressed(GLFW_KEY_F)) {
        followCameraInc.z = -1;
      } else if (mainWindow.isKeyPressed(GLFW_KEY_V)) {
        followCameraInc.z = 1;
      }

    }

    // Switch between free and follow cameras
    if (mainWindow.isKeyPressed(GLFW_KEY_T)) {

      if (!freeCamera && !freeCameraMod) {
        freeCamera = true;
        console.log("Free Camera");
        freeCameraMod = true;
      }
      else if (!freeCameraMod){
        freeCamera = false;
        console.log("Follow Camera");
        world.resetWorldCamera();
        freeCameraMod = true;
      }
    }
    else if (mainWindow.isKeyReleased(GLFW_KEY_Y)) {
      freeCameraMod = false;
    }

    if (mainWindow.isKeyPressed(GLFW_KEY_Y)) {

      if (!wireframe && !wireframeMod) {
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        wireframe = true;
        console.log("Wireframe mode enabled");
        wireframeMod = true;
      }
      else if (!wireframeMod){
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        wireframe = false;
        console.log("Wireframe mode disabled");
        wireframeMod = true;
      }
    }
    else if (mainWindow.isKeyReleased(GLFW_KEY_Y)) {
      wireframeMod = false;
    }

    if (mainWindow.isKeyPressed(GLFW_KEY_U)) {
      if (!culling && !cullingMod) {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        culling = true;
        console.log("Face culling enabled");
        cullingMod = true;
      }
      else if (!cullingMod) {
        glDisable(GL_CULL_FACE);
        culling = false;
        console.log("Face culling disabled");
        cullingMod = true;
      }
    }
    else if (mainWindow.isKeyReleased(GLFW_KEY_U)) cullingMod = false;

    if (mainWindow.isKeyPressed(GLFW_KEY_C)) {
      if (!collisions && !collisionsMod) {
        collisions = true;
        console.log("Collisions enabled");
        collisionsMod = true;
      }
      else if (!collisionsMod) {
        collisions = false;
        console.log("Collisions disabled");
        collisionsMod = true;
      }
    }
    else if (mainWindow.isKeyReleased(GLFW_KEY_C)) collisionsMod = false;

    if (mainWindow.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
      if (!slowedCameraMod) {
        slowedCamera = !slowedCamera;
        slowedCameraMod = true;
      }

    }
    else if (mainWindow.isKeyReleased(GLFW_KEY_LEFT_SHIFT)) slowedCameraMod = false;

    //if (mainWindow.isKeyPressed(GLFW_KEY_7)) world.printTerrainSize();
    /* if (mainWindow.isKeyPressed(GLFW_KEY_7)) {
      System.out.println("Player x: "+world.getPlayer().getGameItem().getPosition().x+" y: "+world.getPlayer().getGameItem().getPosition().z);
    } */
    // Terrain Generation

    if (mainWindow.isKeyPressed(GLFW_KEY_TAB) && !noiseMod) {
      if (!isNoiseRunning) {
        console.log("World Generation Started.");
        startWorldGen();
      }
    }
    if (mainWindow.isKeyReleased(GLFW_KEY_TAB)) {
      noiseMod = false;
    }

    if (mainWindow.getShouldCameraReset()) {
      if (freeCamera) {
        resetCamera();
      }
      else {
        world.resetWorldCamera();
      }
      mainWindow.setShouldCameraReset(false);
    }
  }

  private void startWorldGen() {
    worldGeneratorThread = new Thread(
        new HeightMapGenerator.WorldGenOffloader(new Random().nextLong(), this));
    worldGeneratorThread.start();
    isNoiseRunning = true;
    noiseMod = true;
  }

  @Override
  public void update(float interval) {

    // Trippy mode
    /*
    for (GameItem gameItem : world.getGround()) {
      gameItem.addRotation(0.5f, 0.5f, 0.5f);
    }
    */

    Player p = world.getPlayer();

    if (jumpRequested) {
      p.startJump();
    }
    if (playerRotInc!=0) {
      p.rotatePlayerY(playerRotInc);
    }
    p.movePlayer(playerMovInc);
    if (collisions) p.physicsTick(world.getHeight(p.getGameItem().getPosition())*0.08f-1);
    else p.physicsTick(0);

    if (freeCamera) {
      if (!slowedCamera) {
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        camera.moveRotation(cameraRotInc.x * CAMERA_ROT_STEP, cameraRotInc.y * CAMERA_ROT_STEP, 0);

      }
      else {
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP * 0.15f, cameraInc.y * CAMERA_POS_STEP * 0.15f, cameraInc.z * CAMERA_POS_STEP * 0.15f);
        camera.moveRotation(cameraRotInc.x * CAMERA_ROT_STEP * 0.45f, cameraRotInc.y * CAMERA_ROT_STEP * 0.45f, 0);
      }
    }
    else { // follow camera
      world.addAngleAroundPlayer(followCameraInc.x * CAMERA_AAP_STEP);
      world.addCameraPitch(followCameraInc.y * CAMERA_PITCH_STEP);
      world.addCameraDistance(followCameraInc.z * CAMERA_DISTANCE_STEP);
      world.cameraTick(camera);
    }

    synchronized (worldGeneratorLockObject) {
      if (isNoiseComplete) {
        console.log("World Generation Complete, Loading..");
        isNoiseComplete = false;
        isNoiseRunning = false;
        world.regenerateTerrain(heights);
        heights = null;
      }
    }


  }

  @Override
  public void render(MainWindow mainWindow) {

    renderer.render(mainWindow, camera, world.getMergingIterator());
    world.resetIterator();

  }

  @Override
  public void cleanup() {
    renderer.cleanup();
    for (GameItem gameItem : world.getGameItems()) {
      if (gameItem!=null) gameItem.getMesh().cleanUp();
    }
    console.log("Endless Hiker closed");
  }

  private void resetCamera() {
    camera.setPosition(CAMERA_DEFAULT_POS);
    camera.setRotation(CAMERA_DEFAULT_ROT);
  }

  public void finishLoadingWorld(float[][] heights) {
    synchronized (worldGeneratorLockObject) {
      isNoiseComplete = true;
      this.heights = heights;
    }
  }

}
