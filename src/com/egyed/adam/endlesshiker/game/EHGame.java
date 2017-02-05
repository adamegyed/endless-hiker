package com.egyed.adam.endlesshiker.game;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.engine.GameLogic;
import com.egyed.adam.endlesshiker.engine.MainWindow;
import com.egyed.adam.endlesshiker.engine.graphics.Camera;
import com.egyed.adam.endlesshiker.engine.graphics.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Adam on 2/4/17.
 */
public class EHGame implements GameLogic {

  private final Renderer renderer;

  private GameItem[] gameItems;

  private Camera camera;

  private final Vector3f cameraInc;
  private final Vector3f cameraRotInc;

  private boolean wireframe = false;
  private boolean wireframeMod = false;
  private boolean culling = true;
  private boolean cullingMod = false;

  private boolean slowedCamera;
  private boolean slowedCameraMod;

  // Distances each should move per step
  private static final float CAMERA_POS_STEP = 0.05f;
  private static final float CAMERA_ROT_STEP = 2.0f;
  private static final float SHIFT_STEP = 3f;


  public EHGame() {
    renderer = new Renderer();
    camera = new Camera();
    cameraInc = new Vector3f(0,0,0);
    cameraRotInc = new Vector3f(0,0,0);
  }


  @Override
  public void init(MainWindow mainWindow) throws Exception {
    renderer.init(mainWindow);

    camera.setPosition(0,0,1);

    ArrayList<GameItem> itemList = new ArrayList<>();

    itemList.add(makeCube());

    gameItems = new GameItem[itemList.size()];

    gameItems = itemList.toArray(gameItems);

    glClearColor(0.45f, 0.5f, 0.5f, 0.7f);
  }

  @Override
  public void input(MainWindow mainWindow) {

    cameraInc.set(0, 0, 0);
    cameraRotInc.set(0,0,0);


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
    if (mainWindow.isKeyPressed(GLFW_KEY_Z)) {
      cameraInc.y = -1;
    } else if (mainWindow.isKeyPressed(GLFW_KEY_X)) {
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
    if (mainWindow.isKeyPressed(GLFW_KEY_Y)) {

      if (!wireframe && !wireframeMod) {
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        wireframe = true;
        wireframeMod = true;
      }
      else if (!wireframeMod){
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        wireframe = false;
        wireframeMod = true;
      }
    }
    else if (mainWindow.isKeyReleased(GLFW_KEY_Y)) {
      wireframeMod = false;
    }

    if (mainWindow.isKeyPressed(GLFW_KEY_H)) {
      if (!culling && !cullingMod) {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        culling = true;
        cullingMod = true;
      }
      else if (!cullingMod) {
        glDisable(GL_CULL_FACE);
        culling = false;
        cullingMod = true;
      }
    }
    else if (mainWindow.isKeyReleased(GLFW_KEY_H)) cullingMod = false;

    if (mainWindow.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
      if (!slowedCameraMod) {
        slowedCamera = !slowedCamera;
        slowedCameraMod = true;
      }

    }
    else if (mainWindow.isKeyReleased(GLFW_KEY_LEFT_SHIFT)) slowedCameraMod = false;




    if (mainWindow.getShouldCameraReset()) {
      camera.setPosition(0,0,1);
      camera.setRotation(0,0,0);
      mainWindow.setShouldCameraReset(false);
    }
  }

  @Override
  public void update(float interval) {

    for (GameItem gameItem : gameItems) {
      gameItem.addRotation(0f, 0f, 0f);
    }



    if (!slowedCamera) {
      camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
      camera.moveRotation(cameraRotInc.x * CAMERA_ROT_STEP, cameraRotInc.y * CAMERA_ROT_STEP, 0);

    }
    else {
      camera.movePosition(cameraInc.x * CAMERA_POS_STEP * 0.15f, cameraInc.y * CAMERA_POS_STEP * 0.15f, cameraInc.z * CAMERA_POS_STEP * 0.15f);
      camera.moveRotation(cameraRotInc.x * CAMERA_ROT_STEP * 0.45f, cameraRotInc.y * CAMERA_ROT_STEP * 0.45f, 0);
    }

  }

  @Override
  public void render(MainWindow mainWindow) {

    renderer.render(mainWindow, camera, gameItems);

  }

  @Override
  public void cleanup() {
    renderer.cleanup();
    for (GameItem gameItem : gameItems) {
      if (gameItem!=null) gameItem.getMesh().cleanUp();
    }
  }

  private GameItem makeCube() {

    float[] positions = new float[] {

        // VO
        -0.5f,  0.5f,  0.5f,
        // V1
        -0.5f, -0.5f,  0.5f,
        // V2
        0.5f, -0.5f,  0.5f,
        // V3
        0.5f,  0.5f,  0.5f,
        // V4
        -0.5f,  0.5f, -0.5f,
        // V5
        0.5f,  0.5f, -0.5f,
        // V6
        -0.5f, -0.5f, -0.5f,
        // V7
        0.5f, -0.5f, -0.5f,

    };

    float[] colours = new float[]{
        0.5f, 0.0f, 0.0f,
        0.0f, 0.5f, 0.0f,
        0.0f, 0.0f, 0.5f,
        0.0f, 0.5f, 0.5f,
        0.5f, 0.0f, 0.0f,
        0.0f, 0.5f, 0.0f,
        0.0f, 0.0f, 0.5f,
        0.0f, 0.5f, 0.5f,
    };

    int[] indices = new int[] {



        // Front face
        0, 3, 1, 3, 2, 1,
        // Top Face
        4, 5, 0, 5, 3, 0,
        // Right face
        3, 5, 2, 5, 7, 2,
        // Left face
        4, 0, 6, 0, 1, 6,
        // Bottom face
        6, 7, 1, 7, 2, 1,
        // Back face
        4, 5, 6, 5, 7, 6,

    };

    return new GameItem(new Mesh(positions, colours, indices));
  }

}
