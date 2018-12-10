package bomberman.entity.staticobjects;

import java.util.Vector;

public class Level {
    private static Vector<Wall> walls;

    public static Vector<Wall> getLevel1() {
        walls = new Vector<Wall>();

        addWalls(2,3,4,3);
        addWalls(1,5,2,5);
        addWalls(6,1,6,3);
        addWalls(8,2,11,2);
        addWalls(13,1,13,5);
        addWalls(16,3,17,3);
        addWall(4,5);
        addWall(4,7);
        addWall(4,9);
        addWall(4,11);
        addWall(6,5);
        addWall(6,7);
        addWall(6,9);
        addWall(6,11);
        addWalls(2,13,2,17);
        addWall(3,17);
        addWalls(5,14,5,15);
        addWalls(5,17,8,17);
        addWalls(8,7,8,14);
        addWalls(10,11,10,11);
        addWalls(11,14,12,14);
        addWalls(12,17,12,18);
        addWalls(12,7,16,7);
        addWalls(13,8,13,11);
        addWalls(14,13,15,14);
        addWalls(16,3,17,3);
        addWalls(16,10,17,11);
        addWalls(15,16,18,16);

        return walls;
    }

    private static void addWall(int x, int y) {
        walls.add(new Wall(x * 32, y * 32));
    }

    private static void addWalls(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                walls.add(new Wall(i * 32, j * 32));
            }
        }
    }
}
