package com.egyed.adam.endlesshiker.game.world;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.engine.graphics.Mesh;

/**
 * Created by Adam on 2/4/17.
 */
public class GroundChunk extends GameItem {

  private GroundChunk(Mesh mesh) {
    super(mesh);
  }

  public static GroundChunk createFlat() {

    float[] positions = new float[] {

        // VO
        -4f,  0f,  4f,
        // V1
        4f,  0f,  4f,
        // V2
        -4f,  0f,  -4f,
        // V3
        4f,  0f, -4f,

    };

    float[] colours = new float[]{
        0.01f, 0.6f, 0.0f,
        0.01f, 0.5f, 0.0f,
        0.0f, 0.4f, 0.01f,
        0.01f, 0.5f, 0.0f,
    };

    int[] indices = new int[] {
        //View from the top
        0,1,2, 1, 3, 2,
    };

    Mesh mesh  = new Mesh(positions, colours, indices);

    GroundChunk gc = new GroundChunk(mesh);
    gc.setPosition(0,-0.5f,0);
    return gc;

  }
}
