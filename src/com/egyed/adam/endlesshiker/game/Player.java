package com.egyed.adam.endlesshiker.game;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.engine.graphics.Mesh;
import com.egyed.adam.endlesshiker.engine.graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Created by Adam on 2/4/17.
 * Holds player model and defaults for player
 */
public class Player {

  GameItem model;

  boolean inJump;
  float vVelocity; // Vertical velocity

  private final Vector3f rotation;

  public Player() {
    model = makeCube();
    inJump = false;
    vVelocity = 0f;
    rotation = new Vector3f(0,0,0);
  }

  public GameItem getGameItem() {
    return model;
  }

  public void rotatePlayerY(float yRot) {
    rotation.y += yRot;
  }

  public void movePlayer(Vector2f displacement) {

    // Scale down step distance - cannot cheat to increase speed by travelling diagonally
    if (displacement.x !=0 && displacement.y != 0) {
      displacement.x *= 0.70710678118f;
      displacement.y *= 0.70710678118f;
    }

    Vector2f originalDisplacement = new Vector2f(displacement);

    double yRotRads = Math.toRadians(rotation.y);

    // Idk man it works - translate the two-dimensional vector representing offset relative to the player into
    // a two-dimensional vector representing the offset in world coordinates
    displacement.x = originalDisplacement.x * (float) Math.cos(yRotRads) - originalDisplacement.y * (float) Math.sin(yRotRads);
    displacement.y = originalDisplacement.y * (float) Math.cos(yRotRads) + originalDisplacement.x * (float) Math.sin(yRotRads);


    // Seems incorrect but isn't - displacement.x is x, but disp.y is z
    model.addPosition(displacement.x, 0, displacement.y);
  }

  public void startJump() {
    if (!inJump) {
      vVelocity = 0.8f;
      inJump = true;
    }
  }
  public void physicsTick(float clipHeight) {
    if (inJump) {
      model.getPosition().y += vVelocity;
      vVelocity -= 0.08f;
      if (model.getPosition().y < clipHeight) {
        inJump = false;
        model.getPosition().y = clipHeight;
      }
    }
    else {
     model.getPosition().y = clipHeight;
    }


    model.setRotation(rotation);
  }

  public Vector3f getRotation() {
    return rotation;
  }

  public static GameItem makeCube() {

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

    /*
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
    */

    float[] texCoords = new float[] {
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f,

        1.0f, 0.0f,
        0.0f, 0.0f,
        1.0f, 1.0f,
        0.0f, 1.0f,
    };

    int[] indices = new int[] {



        // Front face
        3, 0, 1, 3, 1, 2,
        // Top Face
        5, 4, 0, 5, 0, 3,
        // Right face
        5, 3, 2, 5, 2, 7,
        // Left face
        0, 4, 6, 0, 6, 1,
        // Bottom face
        6, 7, 1, 7, 2, 1,
        // Back face
        4, 5, 6, 5, 7, 6,

    };
    Texture texture = null;
    try {
      texture = new Texture("/res/texture/cube.png");
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
    return new GameItem(new Mesh(positions, texCoords, indices, texture));
  }

}
