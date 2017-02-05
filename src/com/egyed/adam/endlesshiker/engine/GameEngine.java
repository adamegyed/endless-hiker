package com.egyed.adam.endlesshiker.engine;


/**
 * Created by Adam on 5/10/16.
 * GameEngine manages the fixed-step game loop and interactions between the window and gamelogic
 * also manages timings of frame updates (rendering) and game updates (physics and controls)
 * Will split off into another thread if not on Mac
 */
public class GameEngine implements Runnable{

    // Target Frames per Second
    // Frames can be rendered faster than updates
    // can be more inconsistent than updates
    public static final int TARGET_FPS = 75;

    // Target Updates per Second
    // Updates are calculations on motion, player controls, physics, etc
    // need to be much more consistent with time than frame updates to prevent physics bugs
    public static final int TARGET_UPS = 30;

    private final MainWindow mainWindow;

    private final Timer timer;

    private final GameLogic gameLogic;

    private final Thread gameLoopThread;

    public GameEngine(String windowTitle, int width, int height,
                      boolean vSync, GameLogic gameLogic) throws Exception {

        // Construct the thread for the game loop
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");

        mainWindow = new MainWindow(windowTitle, width, height, vSync);

        this.gameLogic = gameLogic;

        timer = new Timer();

    }

    // Starts the game engine
    public void start() {

        // Avoids starting a second thread if on Mac
        // Still requires -XstartOnFirstThread as JVM argument to start on mac properly
        String osName = System.getProperty("os.name");
        if ( osName.contains("Mac") ) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }


    @Override
    public void run() {
        try {
            init(); // Initialize everything - window, timer, game logic
            gameLoop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            gameLogic.cleanup();
        }

    }

    protected void init() throws Exception {
        mainWindow.init();
        timer.init();
        gameLogic.init(mainWindow);
    }

    protected void gameLoop() {

        // Time since gameloop was run last
        float elapsedTime;

        // Accumulates elapsed time since game loop
        float accumulator = 0f;

        // Time displacement per update
        // 1 / (updates/second) = seconds/update = 1/30
        float interval = 1f / TARGET_UPS;

        while (!mainWindow.windowShouldClose()) {
            elapsedTime = timer.getEllapsedTime();

            accumulator += elapsedTime;

            input();

            // While the accumulator has at least as many nanoseconds in it as the update interval
            while (accumulator >= interval) {

                // Update once
                update(interval);

                // Take one interval per update away from the accumulator
                accumulator -= interval;

                // repeat if more than one interval was accumulated (elapsed) since the rendering time
            }

            // Render to the window
            render();

            // Sync time if not in vSync mode
            if (!mainWindow.isvSync()) {
                sync();
            }
        }

    }

    // Wait until next (render) frame is needed, as defined by the target fps
    private void sync() {

        float loopSlot = 1f / TARGET_FPS;

        // Time when next frame is needed
        double endTime = timer.getLastLoopTime() + loopSlot;

        while (timer.getTime() < endTime) {
            try {
                // Repeatedly micro-sleep until the current time reaches the ending time
                Thread.sleep(1);

            } catch (InterruptedException ie) {
                ie.printStackTrace();
                System.exit(-1);
            }
        }
    }

    protected void input() {
        gameLogic.input(mainWindow);
    }

    protected void update(float interval) {
        gameLogic.update(interval);
    }

    protected void render() {
        gameLogic.render(mainWindow);
        mainWindow.update();
    }
}
