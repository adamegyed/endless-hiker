package com.egyed.adam.endlesshiker.game.worldgen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Adam on 4/28/17.
 */
public class HeightMapGenerator {

  private static String filePath = "src/save/map.png";

  private static final int WIDTH = 64;
  private static final int HEIGHT = 64;
  private static final double FEATURE_SIZE = 24;

  public static String genMap(long seed) {
    OpenSimplexNoise noise = new OpenSimplexNoise(seed);
    BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
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
      ImageIO.write(image, "png", new File(filePath));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }


    return filePath;
  }

  public static void main(String[] args) {
    genMap(123456475);
  }
}
