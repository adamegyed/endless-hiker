package com.egyed.adam.endlesshiker.game;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.engine.graphics.Mesh;
import com.egyed.adam.endlesshiker.engine.graphics.Texture;
import org.joml.Vector2f;

/**
 * Created by Adam on 2/4/17.
 * Holds player model and defaults for player
 */
public class Player {
  GameItem model;

  boolean inJump;
  float vVelocity;

  public Player() {
    model = makeCube();
    inJump = false;
    vVelocity = 0f;
  }

  public GameItem getGameItem() {
    return model;
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

  public void movePlayer(Vector2f displacement) {
    if (displacement.x!=0 && displacement.y != 0) {
      displacement.x *= 0.70710678118f;
      displacement.y *= 0.70710678118f;
    }
    model.setPosition(model.getPosition().x + displacement.x, model.getPosition().y, model.getPosition().z + displacement.y);
  }

  public void startJump() {
    if (!inJump) {
      vVelocity = 0.8f;
      inJump = true;
    }
  }
  public void jumpTick() {
    if (inJump) {
      model.getPosition().y += vVelocity;
      vVelocity -= 0.08f;
      if (model.getPosition().y<0) {
        inJump = false;
        model.getPosition().y = 0;
      }
    }
  }

}
