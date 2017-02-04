package com.egyed.adam.endlesshiker;



/**
 * Created by Adam on 5/10/16.
 * Unifying Interface for any game to be used with GameEngine
 */
public interface GameLogic {

    // Game state initialization
    void init(MainWindow mainWindow) throws Exception;

    // Input handling and responses
    void input(MainWindow mainWindow);

    // Physics and world update
    void update(float interval);

    // Render contents
    void render(MainWindow mainWindow);

    void cleanup();



}
