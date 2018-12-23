package bomberman;

import bomberman.constants.GlobalConstants;
import bomberman.entity.Entity;
import bomberman.entity.player.Player;
import bomberman.entity.staticobjects.BlackBomb;
import bomberman.gamecontroller.InputManager;
import bomberman.scenes.Sandbox;

import java.util.Iterator;
import java.util.Vector;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class GameLoop {

    static double currentGameTime;
    static double oldGameTime;
    static double deltaTime;
    final static long startNanoTime = System.nanoTime();

    public static double getCurrentGameTime() {
        return currentGameTime;
    }

    public static void start(GraphicsContext gc) {
        GameState.gameStatus = GlobalConstants.GameStatus.Running;
        //Here is doing the game loop, AnimationTimer will be called every frame
        //Here need a AnimationTimer.stop() method to stop the game loop
        new AnimationTimer() {
            //Overload
            public void handle(long currentNanoTime) {
                oldGameTime = currentGameTime;
                currentGameTime = (currentNanoTime - startNanoTime) / 1000000000.0;
                deltaTime = currentGameTime - oldGameTime;
                gc.clearRect(0, 0, GlobalConstants.CANVAS_WIDTH, GlobalConstants.CANVAS_WIDTH);
                //TODO This will have to be something like, currentScene.getEntities()
                updateGame();
                renderGame();
            }
        }.start();
    }

    public static double getDeltaTime() {
        return deltaTime * 100;
    }

    public static void updateGame() {
        InputManager.handlePlayerMovements();
        Vector<Entity> entities = Sandbox.getEntities();
        Iterator<Entity> it = entities.iterator();
        //remove the current bomb
        while (it.hasNext()) {
            Entity entity = it.next();

            if (entity instanceof BlackBomb) {
                BlackBomb bomb = (BlackBomb) entity;
                boolean alive = bomb.isAlive();

                if (!alive) {
                    // not removig directly from list to prevent ConcurrentModification
                    if (bomb.getBelong() == Sandbox.getPlayer1()) {
                        Sandbox.getPlayer1().limitDecrease();
                    } else {
                        Sandbox.getPlayer2().limitDecrease();
                    }
                    it.remove();
                    Sandbox.addExplosion(0, bomb);
                }
            }
        }
    }

    public static void renderGame() {
        //draw all the entity in a frame
        for (Entity e : Sandbox.getEntities()) {
            e.draw();
        }
    }

}
