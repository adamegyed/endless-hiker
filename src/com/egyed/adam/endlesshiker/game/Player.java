package com.egyed.adam.endlesshiker.game;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.engine.graphics.Mesh;
import com.egyed.adam.endlesshiker.engine.graphics.Texture;

/**
 * Created by Adam on 2/4/17.
 */
public class Player {
  GameItem model;

  public Player() {
    model = makeCube();
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
    Texture texture = new Texture("file_name")
    return new GameItem(new Mesh(positions, colours, indices, texture)); // FIX!!! Need textcoords instead of colours
  }

}
