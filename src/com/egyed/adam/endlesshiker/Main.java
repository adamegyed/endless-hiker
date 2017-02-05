package com.egyed.adam.endlesshiker;

import com.egyed.adam.endlesshiker.engine.GameEngine;
import com.egyed.adam.endlesshiker.engine.GameLogic;

public class Main {

    public static void main(String[] args) {
      try {

        GameLogic gameLogic = new com.egyed.adam.endlesshiker.game.EHGame();

        // Construct the Game Engine with the game logic defined by 'gameLogic', an instance of RubiksGame
        GameEngine gameEngine = new GameEngine("Endless Hiker", 800, 600, true, gameLogic);

        gameEngine.start();

      } catch (Exception e) {

        e.printStackTrace();
        System.exit(-1);

      }
    }
}
