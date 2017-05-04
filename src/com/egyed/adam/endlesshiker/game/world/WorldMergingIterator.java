package com.egyed.adam.endlesshiker.game.world;

import com.egyed.adam.endlesshiker.engine.GameItem;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Adam on 5/3/17.
 */
public class WorldMergingIterator implements Iterator<GameItem> {

  private int index = 0;

  private GameItem[] regularItems;

  private Iterator<GameItem> terrainIterator;

  public WorldMergingIterator(GameItem[] gameItems, Iterator<GameItem> terrainIterator) {
    this.regularItems = gameItems;
    this.terrainIterator = terrainIterator;
  }

  public void reset(Iterator<GameItem> terrainIterator) {
    index = 0;
    this.terrainIterator = terrainIterator;
  }

  @Override
  public boolean hasNext() {
    return terrainIterator.hasNext() || index < regularItems.length;
  }

  @Override
  public GameItem next() {
    if (terrainIterator.hasNext()) return terrainIterator.next();
    else {
      if (index >= regularItems.length) throw new NoSuchElementException();
      return regularItems[index++];
    }
  }
}
