package com.egyed.adam.endlesshiker.game.world;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.engine.graphics.Camera;
import com.egyed.adam.endlesshiker.engine.graphics.HeightMapMesh;
import com.egyed.adam.endlesshiker.game.Player;
import com.egyed.adam.endlesshiker.game.worldgen.HeightMapGenerator;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Adam on 2/4/17.
 * Controls currently loaded GameItems and world interactions
 */
public class World {

  private Player player;

  private LinkedList<GameItem> ground;

  private GameItem[] gameItems;

  private float cameraDistance;
  private float cameraPitch;
  private float cameraAngleAroundPlayer;

  public static final float MAX_CAMERA_PITCH = 90f;
  public static final float MIN_CAMERA_PITCH = 0f;

  public static final float MIN_CAMERA_DISTANCE = 1.0f;
  public static final float MAX_CAMERA_DISTANCE = 20f;

  public static final float DEFAULT_CAMERA_DISTANCE = 5f;
  public static final float DEFAULT_CAMERA_PITCH = 40f;

  private WorldMergingIterator mergingIterator;

  private float[][] heights = new float[256][256];

  public World() {
    cameraAngleAroundPlayer = 0f;
    cameraPitch = DEFAULT_CAMERA_PITCH;
    cameraDistance = DEFAULT_CAMERA_DISTANCE;

    ground = new LinkedList<>();

  }

  public void cameraTick(Camera c) {
    float horizontalDistance = cameraDistance * (float) Math.cos(Math.toRadians(cameraPitch));
    float verticalDistance = cameraDistance * (float) Math.sin(Math.toRadians(cameraPitch));


    c.setRotation(cameraPitch, player.getRotation().y + 180 + cameraAngleAroundPlayer, 0);

    Vector3f playerPosition = player.getGameItem().getPosition();
    c.setPosition(playerPosition.x + horizontalDistance * (float)
        Math.sin(Math.toRadians(player.getRotation().y + cameraAngleAroundPlayer)),
        playerPosition.y + verticalDistance,
        playerPosition.z - horizontalDistance * (float)
            Math.cos(Math.toRadians(player.getRotation().y +
            cameraAngleAroundPlayer)));

  }

  public void init() {
    ArrayList<GameItem> itemList = new ArrayList<>(10);

    player = new Player();
    itemList.add(player.getGameItem());

    gameItems = new GameItem[itemList.size()];

    gameItems = itemList.toArray(gameItems);

    generateTerrain();

    mergingIterator = new WorldMergingIterator(gameItems, ground.iterator());


  }

  private void generateTerrain() {

    HeightMapMesh heightMap = null;

    int blocksPerRow = 1;

    float scale = 120;

    try {
      heightMap = new HeightMapMesh(0.0f, 0.10f, "/save/map.png",
          "/res/texture/grasstile.png", 16);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }

    GameItem[] terrainItems = new GameItem[blocksPerRow * blocksPerRow];

    for (int row = 0; row < blocksPerRow; row++) {
      for (int col = 0; col < blocksPerRow; col++) {
        float xDisplacement = (col - ((float) blocksPerRow - 1) / (float) 2) * scale * HeightMapMesh.getXLength();
        float zDisplacement = (row - ((float) blocksPerRow - 1) / (float) 2) * scale * HeightMapMesh.getZLength();

        GameItem terrainBlock = new GameItem(heightMap.getMesh());
        terrainBlock.setScale(scale);
        terrainBlock.setPosition(xDisplacement, 5, zDisplacement);
        terrainItems[row * blocksPerRow + col] = terrainBlock;
      }
    }

    ground = new LinkedList<>(Arrays.asList(terrainItems));

    /*ground = GroundChunk.createFlat();
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(8.0f,-0.5f *//*random.nextFloat() * 0.5f*//*,0);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(8.0f,-0.5f *//*random.nextFloat() * 0.5f*//*,8.0f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(8.0f,-0.5f *//*random.nextFloat() * 0.5f*//*,-8.0f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(-8.0f,-0.5f *//*random.nextFloat() * 0.5f*//*,0);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(-8.0f,-0.5f *//*random.nextFloat() * 0.5f*//*,8.0f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(-8.0f,-0.5f *//*random.nextFloat() * 0.5f*//*,-8.0f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(0f,-0.5f *//*random.nextFloat() * 0.5f*//*,8.0f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(0f,-0.5f *//*random.nextFloat() * 0.5f*//*,-8.0f);
    itemList.add(ground);

    itemList.add(Player.makeCube());*/

  }

  public void regenerateTerrain(float[][] heights) {

    this.heights = heights;

    HeightMapMesh heightMap = null;
    try {
      heightMap = new HeightMapMesh(0.0f, 0.10f, heights,
          "/res/texture/grasstile.png", 16);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }

    int blocksPerRow = 1;
    float scale = 120;

    GameItem[] terrainItems = new GameItem[blocksPerRow * blocksPerRow];

    for (int row = 0; row < blocksPerRow; row++) {
      for (int col = 0; col < blocksPerRow; col++) {
        float xDisplacement = (col - ((float) blocksPerRow - 1) / (float) 2) * scale * HeightMapMesh.getXLength();
        float zDisplacement = (row - ((float) blocksPerRow - 1) / (float) 2) * scale * HeightMapMesh.getZLength();

        GameItem terrainBlock = new GameItem(heightMap.getMesh());
        terrainBlock.setScale(scale);
        terrainBlock.setPosition(xDisplacement, -1, zDisplacement);
        terrainItems[row * blocksPerRow + col] = terrainBlock;
      }
    }

    ground = new LinkedList<>(Arrays.asList(terrainItems));

    resetIterator();

  }

  public GameItem[] getGameItems() {
    return gameItems;
  }

  public Player getPlayer() {
    return player;
  }

  public void printTerrainSize() {
    System.out.println("Ground list size = "+ground.size());
  }

  public WorldMergingIterator getMergingIterator() {
    return mergingIterator;
  }

  public void resetIterator() {
    mergingIterator.reset(ground.iterator());
  }

  public float getCameraAngleAroundPlayer() {
    return cameraAngleAroundPlayer;
  }

  public float getCameraPitch() {
    return cameraPitch;
  }

  public float getCameraDistance() {
    return cameraDistance;
  }

  public void resetWorldCamera() {
    cameraAngleAroundPlayer = 0f;
    cameraDistance = DEFAULT_CAMERA_DISTANCE;
    cameraPitch = DEFAULT_CAMERA_PITCH;
  }

  public void addCameraPitch(float angle) {
    cameraPitch += angle;
    if (cameraPitch > MAX_CAMERA_PITCH) cameraPitch = MAX_CAMERA_PITCH;
    if (cameraPitch < MIN_CAMERA_PITCH) cameraPitch = MIN_CAMERA_PITCH;
  }

  public void addAngleAroundPlayer(float angle) {
    cameraAngleAroundPlayer += angle;
    if (cameraAngleAroundPlayer > 360) cameraAngleAroundPlayer -= 360f;
    if (cameraAngleAroundPlayer < 0) cameraAngleAroundPlayer += 360f;
  }

  public void addCameraDistance(float d) {
    cameraDistance += d;
    if (cameraDistance > MAX_CAMERA_DISTANCE) cameraDistance = MAX_CAMERA_DISTANCE;
    if (cameraDistance < MIN_CAMERA_DISTANCE) cameraDistance = MIN_CAMERA_DISTANCE;
  }



  public float getHeight(Vector3f position) {
    float result;

    Vector3f[] triangle = getTriangle(position, ground.getFirst());
    result = interpolateHeight(triangle[0], triangle[1], triangle[2], position.x, position.z);

    return result;
  }

  protected Vector3f[] getTriangle(Vector3f position, GameItem terrainBlock) {
    // Get the column and row of the heightmap associated to the current position
    float cellWidth = 1f;
    float cellHeight = 1f;

    float otherThing = -120f; // -120f

    int col = (int) ((position.x - otherThing) / cellWidth);
    int row = (int) ((position.z - otherThing) / cellHeight);

    Vector3f[] triangle = new Vector3f[3];
    triangle[1] = new Vector3f(
        otherThing + col * cellWidth,
        getWorldHeight(row + 1, col, terrainBlock),
        otherThing + (row + 1) * cellHeight);
    triangle[2] = new Vector3f(
        otherThing + (col + 1) * cellWidth,
        getWorldHeight(row, col + 1, terrainBlock),
        otherThing + row * cellHeight);
    if (position.z < getDiagonalZCoord(triangle[1].x, triangle[1].z, triangle[2].x, triangle[2].z, position.x)) {
      triangle[0] = new Vector3f(
          otherThing + col * cellWidth,
          getWorldHeight(row, col, terrainBlock),
          otherThing + row * cellHeight);
    } else {
      triangle[0] = new Vector3f(
          otherThing + (col + 1) * cellWidth,
          getWorldHeight(row + 2, col + 1, terrainBlock),
          otherThing + (row + 1) * cellHeight);
    }

    return triangle;
  }

  protected float interpolateHeight(Vector3f pA, Vector3f pB, Vector3f pC, float x, float z) {
    // Plane equation ax+by+cz+d=0
    float a = (pB.y - pA.y) * (pC.z - pA.z) - (pC.y - pA.y) * (pB.z - pA.z);
    float b = (pB.z - pA.z) * (pC.x - pA.x) - (pC.z - pA.z) * (pB.x - pA.x);
    float c = (pB.x - pA.x) * (pC.y - pA.y) - (pC.x - pA.x) * (pB.y - pA.y);
    float d = -(a * pA.x + b * pA.y + c * pA.z);
    // y = (-d -ax -cz) / b
    float y = (-d - a * x - c * z) / b;
    return y;
  }

  private float getWorldHeight(int row, int col, GameItem gameItem) {
    float y = getHeightFromArray(row, col);
    return y * gameItem.getScale() + gameItem.getPosition().y;
  }

  public float getHeightFromArray(int row, int col) {
    float result = 0;
    if ( row >= 0 && row < heights.length ) {
      if ( col >= 0 && col < heights[row].length ) {
        result = heights[row][col];
      }
    }
    return result;
  }

  protected float getDiagonalZCoord(float x1, float z1, float x2, float z2, float x) {
    float z = ((z1 - z2) / (x1 - x2)) * (x - x1) + z1;
    return z;
  }

  public LinkedList<GameItem> getGround() {
    return ground;
  }
}
