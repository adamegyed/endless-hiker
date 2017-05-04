package com.egyed.adam.endlesshiker.game.worldgen;

import com.egyed.adam.endlesshiker.engine.graphics.HeightMapMesh;
import com.egyed.adam.endlesshiker.game.EHGame;
import com.egyed.adam.endlesshiker.game.world.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Adam on 4/28/17.
 */
public class HeightMapGenerator {

  private static String filePath = "src/save/map.png";

  private static final int WIDTH = 256;
  private static final int HEIGHT = 256;
  private static final double FEATURE_SIZE = 24;

  public static String genMapOld(long seed) {
    OpenSimplexNoise noise = new OpenSimplexNoise(seed);
    System.out.println("Pre - buffered image construction");
    System.out.flush();
    BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    System.out.println("Post - buffered image construction");
    System.out.flush();
    for (int y = 0; y < HEIGHT; y++)
    {
      for (int x = 0; x < WIDTH; x++)
      {
        double value = noise.eval(x / FEATURE_SIZE, y / FEATURE_SIZE, 0.0);
        int rgb = 0x010101 * (int)((value + 1) * 127.5);
        image.setRGB(x, y, rgb);
      }
    }

    try {
      System.out.println("Begin write");
      ImageIO.write(image, "png", new File(filePath));
      System.out.print("ENd Write");
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }


    return filePath;
  }

  public static float[][] genMap(long seed) {
    OpenSimplexNoise noise = new OpenSimplexNoise(seed);

    float[][] result = new float[WIDTH][HEIGHT];
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        double value = noise.eval(x / FEATURE_SIZE, y / FEATURE_SIZE, 0.0);
        result[x][y] = (float) value;
      }
    }

    return result;
  }

  public static class WorldGenOffloader implements Runnable {

    private long seed;
    private EHGame gameEngine;

    public WorldGenOffloader(long seed, EHGame onceFinished) {
      this.seed = seed;
      this.gameEngine = onceFinished;
    }

    @Override
    public void run() {
      //JOptionPane.showInputDialog(null,"Please enter a seed","Endless Hiker", JOptionPane.QUESTION_MESSAGE);
      gameEngine.finishLoadingWorld(genMap(seed));
    }
  }

  public static void main(String[] args) {
    genMap(123456475);
  }
}
