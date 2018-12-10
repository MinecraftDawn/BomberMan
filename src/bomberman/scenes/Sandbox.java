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

import java.util.Vector;

import bomberman.GameLoop;
import bomberman.Renderer;
import bomberman.entity.Entity;
import bomberman.entity.player.Player;
import bomberman.entity.staticobjects.BlackBomb;
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
    private static Vector<BlackBomb> bombList = new Vector<BlackBomb>();
    private static Vector<Player> playerList = new Vector<Player>();

    public static Vector<Entity> getEntities() {
        return entities;
    }

    public static boolean addEntityToGame(Entity e) {

        if (e instanceof BlackBomb) {
            if (((BlackBomb)e).getBelong().getLimit() < 5) {
//                System.out.println(sandboxPlayer1.getLimit());

                for (Entity entity : entities) {
                    if (entity.getPositionX() == e.getPositionX() && entity.getPositionY() == e.getPositionY()) {
                        System.out.println("沒放到炸彈");
                        return false;
                    }
                }

                if (((BlackBomb)e).getBelong() == sandboxPlayer1)
                    sandboxPlayer1.limitIncrease();
                else
                    sandboxPlayer2.limitIncrease();
            }
            else {
                System.out.println("p1");
                System.out.println("p2");
                System.out.println("超過系統限制");
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
        sandboxPlayer2 = p2;
        addEntityToGame(p2);
    }

    public static Player getPlayer() {
        return sandboxPlayer1;
    }

    public static Player getPlayer2() {
        return sandboxPlayer2;
    }
}
