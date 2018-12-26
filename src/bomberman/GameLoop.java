package bomberman;

import bomberman.constants.GlobalConstants;
import bomberman.entity.Entity;
import bomberman.entity.player.Player;
import bomberman.entity.staticobjects.BlackBomb;
import bomberman.entity.staticobjects.Explosion;
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
        Vector<BlackBomb> deadBomb = new Vector<>();//儲存已死亡的炸彈，用來產生爆炸實體
        //remove the current bomb
        while (it.hasNext()) {
            Entity entity = it.next();

            if (entity instanceof BlackBomb) {
                BlackBomb bomb = (BlackBomb) entity;
                boolean alive = bomb.isAlive();

                if (bomb.isExploded()) {
                    it.remove();
                    if (bomb.getBelong() == Sandbox.getPlayer1()) {
                        Sandbox.getPlayer1().limitDecrease();
                    } else {
                        Sandbox.getPlayer2().limitDecrease();
                    }
                } else if (!alive) {
                    // not removig directly from list to prevent ConcurrentModification
                    if (bomb.getBelong() == Sandbox.getPlayer1()) {
                        Sandbox.getPlayer1().limitDecrease();
                    } else {
                        Sandbox.getPlayer2().limitDecrease();
                    }
                    deadBomb.add(bomb);
                    it.remove();
                    Sandbox.bombList.remove(0);
                }
                //如果實體是爆炸
            } else if (entity instanceof Explosion) {
                Explosion explosion = (Explosion) entity;
                boolean alive = (!explosion.getState().equals(Explosion.STATE.EXPLOD_END));

                if (!alive) {
                    it.remove();
                    Sandbox.explosionList.remove(0);
                }
            } else if (entity instanceof Player) {
                Player p = (Player) entity;

                Vector<Explosion> explosions = Sandbox.getExplosionList();
                for(Explosion e : explosions){
                    if(p.isColliding(e)){
                        p.damage(100);
                    }
                }
                if (!p.isAlive())
                    it.remove();
            }
        }
        //把爆炸的實體放入entitys List內
        Iterator<BlackBomb> bombIt = deadBomb.iterator();
        while (bombIt.hasNext()) {
            Sandbox.addExplosion(bombIt.next());
            bombIt.remove();
        }
    }

    public static void renderGame() {
        //draw all the entity in a frame
        for (Entity e : Sandbox.getEntities()) {
            e.draw();
        }
    }

}
