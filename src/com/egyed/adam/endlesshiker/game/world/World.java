package com.egyed.adam.endlesshiker.game.world;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.engine.graphics.Camera;
import com.egyed.adam.endlesshiker.game.Player;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Adam on 2/4/17.
 * Controls currently loaded GameItems and world interactions
 */
public class World {

  private Player player;

  private GameItem ground;

  private GameItem[] gameItems;

  private float cameraDistance;
  private float cameraPitch;
  private float cameraAngleAroundPlayer;

  public static final float MAX_CAMERA_PITCH = 90f;
  public static final float MIN_CAMERA_PITCH = 0f;

  public static final float MIN_CAMERA_DISTANCE = 1.0f;
  public static final float MAX_CAMERA_DISTANCE = 20f;

  public static final float DEFAULT_CAMERA_DISTANCE = 4f;
  public static final float DEFAULT_CAMERA_PITCH = 40f;

  public World() {
    cameraAngleAroundPlayer = 0f;
    cameraPitch = DEFAULT_CAMERA_PITCH;
    cameraDistance = DEFAULT_CAMERA_DISTANCE;

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


    Random random = new Random();
    random.setSeed(random.nextLong());

    player = new Player();
    itemList.add(player.getGameItem());


    ground = GroundChunk.createFlat();
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(8.0f,-0.5f /*random.nextFloat() * 0.5f*/,0);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(8.0f,-0.5f /*random.nextFloat() * 0.5f*/,8.0f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(8.0f,-0.5f /*random.nextFloat() * 0.5f*/,-8.0f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(-8.0f,-0.5f /*random.nextFloat() * 0.5f*/,0);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(-8.0f,-0.5f /*random.nextFloat() * 0.5f*/,8.0f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(-8.0f,-0.5f /*random.nextFloat() * 0.5f*/,-8.0f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(0f,-0.5f /*random.nextFloat() * 0.5f*/,8.0f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(0f,-0.5f /*random.nextFloat() * 0.5f*/,-8.0f);
    itemList.add(ground);


    gameItems = new GameItem[itemList.size()];
    gameItems = itemList.toArray(gameItems);
  }

  public GameItem[] getGameItems() {
    return gameItems;
  }

  public Player getPlayer() {
    return player;
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



}
