package com.egyed.adam.endlesshiker.game.world;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.game.Player;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Adam on 2/4/17.
 */
public class World {

  private Player player;

  private GameItem ground;

  private GameItem[] gameItems;

  public World() {

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
    ground.setPosition(8.1f,random.nextFloat() * 0.5f,0);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(8.1f,random.nextFloat() * 0.5f,8.1f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(8.1f,random.nextFloat() * 0.5f,-8.1f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(-8.1f,random.nextFloat() * 0.5f,0);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(-8.1f,random.nextFloat() * 0.5f,8.1f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(-8.1f,random.nextFloat() * 0.5f,-8.1f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(0f,random.nextFloat() * 0.5f,8.1f);
    itemList.add(ground);

    ground = GroundChunk.createFlat();
    ground.setPosition(0f,random.nextFloat() * 0.5f,-8.1f);
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

}
