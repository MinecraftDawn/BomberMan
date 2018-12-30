/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.scenes;

import static bomberman.constants.GlobalConstants.CANVAS_HEIGHT;
import static bomberman.constants.GlobalConstants.CANVAS_WIDTH;
import static bomberman.constants.GlobalConstants.SCENE_HEIGHT;
import static bomberman.constants.GlobalConstants.SCENE_WIDTH;

import java.util.Iterator;
import java.util.Vector;

import bomberman.GameLoop;
import bomberman.Renderer;
import bomberman.entity.Entity;
import bomberman.entity.player.Player;
import bomberman.entity.staticobjects.BlackBomb;
import bomberman.entity.staticobjects.Explosion;
import bomberman.entity.staticobjects.Level;
import bomberman.entity.staticobjects.Wall;
import bomberman.gamecontroller.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Ashish
 */
public class Sandbox {

    static Scene scene;
    static Group root;
    static Canvas canvas;
    static GraphicsContext graphicsContext;
    private static boolean sceneStarted;
    static Player sandboxPlayer1, sandboxPlayer2;

    static {
        sceneStarted = false;
    }

    private static Vector<Entity> entities = new Vector<Entity>();
//    public static Vector<BlackBomb> bombList = new Vector<BlackBomb>();
    public static Vector<Player> playerList = new Vector<Player>();
    public static Vector<Explosion> explosionList = new Vector<Explosion>();

    public static Vector<Entity> getEntities() {
        return entities;
    }

    public static boolean addEntityToGame(Entity e) {

        if (e instanceof BlackBomb) {
            BlackBomb bomb = (BlackBomb) e;

            if (bomb.getBelong().getLimit() < 5) {

                for (Entity entity : entities) {
                    if (entity.getPositionX() == e.getPositionX() && entity.getPositionY() == e.getPositionY()) {
                        return false;
                    }
                }

                if (bomb.getBelong() == sandboxPlayer1) {
                    sandboxPlayer1.limitIncrease();
                } else {
                    sandboxPlayer2.limitIncrease();
                }

//                bombList.add(bomb);
            } else {
                return false;
            }
        }

        if (!entities.contains(e)) {
            entities.add(e);
            return true;
        } else {
            return false;
        }
    }

    public static void addExplosion(BlackBomb bomb) {
        int x = bomb.getPositionX();
        int y = bomb.getPositionY();
        boolean upwardHasWall = false;
        boolean leftwardHasWall = false;
        boolean rightwardHasWall = false;
        boolean downwardHasWall = false;
        int power = bomb.getBelong().getPower();

        //由中間向四個方向出發
        for (int i = 0; i <= power; i++) {
            Explosion upExplosion;
            Explosion downExplosion;
            Explosion leftExplosion;
            Explosion rightExplosion;
            //因為同時要判斷wall以及bomb所以使用entities
            Iterator<Entity> it = entities.iterator();
            Vector<BlackBomb> explode = new Vector<BlackBomb>();

            while (it.hasNext()) {
                Entity e = it.next();

                if (e instanceof BlackBomb) {
                    BlackBomb theBomb = (BlackBomb)e;
                    int bombx = theBomb.positionX;
                    int bomby = theBomb.positionY;

                    //牆後的炸彈不應該被引爆
                    if ((bombx == x + i * 32 && bomby == y && !rightwardHasWall) || (bombx == x && bomby == y + i * 32 && !upwardHasWall)
                            || (bombx == x - i * 32 && bomby == y && !leftwardHasWall) || (bombx == x && bomby == y - i * 32 && !downwardHasWall)) {
                        //記得減掉player的limit
                        if (theBomb.getBelong() == getPlayer1()) {
                            getPlayer1().limitDecrease();
                        } else {
                            getPlayer2().limitDecrease();
                        }
                        //這裡刪除的是entities裡的物件
                        it.remove();
                        theBomb.setBombState(BlackBomb.STATE.DEAD);
                        explode.add(theBomb);
                    }
                }

                if (e instanceof Wall) {
                    Wall theWall = (Wall)e;
                    int wallx = theWall.positionX;
                    int wally = theWall.positionY;

                    if (wallx == x + i * 32 && wally == y)
                        rightwardHasWall = true;
                    else if (wallx == x && wally == y + i * 32)
                        upwardHasWall = true;
                    else if (wallx == x - i * 32 && wally == y)
                        leftwardHasWall = true;
                    else if (wallx == x && wally == y - i * 32)
                        downwardHasWall = true;
                }
            }

            Iterator<BlackBomb> bombIt = explode.iterator();
            while (bombIt.hasNext()) {
                BlackBomb theBomb = bombIt.next();
                addExplosion(theBomb);
            }

            if (i != 0) {
                if (!upwardHasWall) {
                    upExplosion = new Explosion(bomb, x, y + i * 32);
                    explosionList.add(upExplosion);
                    addEntityToGame(upExplosion);

                }
                if (!downwardHasWall) {
                    downExplosion = new Explosion(bomb, x, y - i * 32);
                    explosionList.add(downExplosion);
                    addEntityToGame(downExplosion);
                }
                if (!leftwardHasWall) {
                    leftExplosion = new Explosion(bomb, x - i * 32, y);
                    explosionList.add(leftExplosion);
                    addEntityToGame(leftExplosion);
                }
                if (!rightwardHasWall) {
                    rightExplosion = new Explosion(bomb, x + i * 32, y);
                    explosionList.add(rightExplosion);
                    addEntityToGame(rightExplosion);
                }
            } else {
                upExplosion = new Explosion(bomb, x, y);
                explosionList.add(upExplosion);
                addEntityToGame(upExplosion);
            }
        }
    }

    private static void init() {
        root = new Group();
        scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        root.getChildren().add(canvas);
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(2);
        graphicsContext.setFill(Color.BLUE);
//        Renderer.init();
        GameLoop.start(graphicsContext);

        //Initialize Objects
        Player p1 = new Player(1);
        Player p2 = new Player(2);
        setPlayer(p1, p2);

        //load map
        loadMap();

        //should be called at last it based on player
        EventHandler.attachEventHandlers(scene);

    }


    //Eventually this should take some kind of map input, maybe a text file or something
    public static void loadMap() {
        Vector<Wall> walls = new Vector<Wall>();
        //width and length both have 20 blocks
        //一格有32單位的長度
        for (int i = 0; i < SCENE_WIDTH; i += 32)
            for (int j = 0; j < SCENE_HEIGHT; j += 32)
                if (i == 0 || i + 33 > SCENE_HEIGHT || j == 0 || j + 33 > SCENE_WIDTH)
                    walls.add(new Wall(i, j));

        for (Wall wall : Level.getLevel1()) {
            walls.add(wall);
        }

        for (Wall wall : walls) {
            addEntityToGame(wall);
        }

    }

    public static void setupScene() {
        if (!sceneStarted) {
            init();
            sceneStarted = true;
        }
    }

    public static Scene getScene() {
        return scene;
    }

    public static GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public static Canvas getCanvas() {
        return canvas;
    }

    public static void setPlayer(Player p1, Player p2) {
        sandboxPlayer1 = p1;
        addEntityToGame(p1);
        playerList.add(p1);
        sandboxPlayer2 = p2;
        addEntityToGame(p2);
        playerList.add(p2);
    }

    public static Player getPlayer1() {
        return sandboxPlayer1;
    }

    public static Player getPlayer2() {
        return sandboxPlayer2;
    }

    public static Vector<Explosion> getExplosionList() {
        return explosionList;
    }
}
