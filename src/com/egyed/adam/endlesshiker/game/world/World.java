package com.egyed.adam.endlesshiker.game.world;

import com.egyed.adam.endlesshiker.engine.GameItem;
import com.egyed.adam.endlesshiker.game.Player;

import java.util.ArrayList;

/**
 * Created by Adam on 2/4/17.
 */
public class World {

  private Player player;

  private GameItem[] gameItems;

  public World() {

  }

  public void init() {
    player = new Player();


    ArrayList<GameItem> itemList = new ArrayList<>();

    itemList.add(player.getGameItem());

    gameItems = new GameItem[itemList.size()];

    gameItems = itemList.toArray(gameItems);
  }

  public GameItem[] getGameItems() {
    return gameItems;
  }
}
