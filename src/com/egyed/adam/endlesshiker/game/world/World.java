package com.egyed.adam.endlesshiker.game.world;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.game.Player;
import org.joml.Vector2f;

import java.util.ArrayList;

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
    ArrayList<GameItem> itemList = new ArrayList<>();


    player = new Player();
    itemList.add(player.getGameItem());


    ground = GroundChunk.createFlat();
    itemList.add(ground);


    gameItems = new GameItem[itemList.size()];
    gameItems = itemList.toArray(gameItems);
  }

  public GameItem[] getGameItems() {
    return gameItems;
  }

  public void movePlayer(Vector2f displacement) {
    player.movePlayer(displacement);
  }
}
