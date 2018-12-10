package bomberman.entity.staticobjects;

import bomberman.animations.Sprite;
import bomberman.entity.Entity;
import bomberman.entity.boundedbox.RectBoundedBox;

public class Explosion implements Entity {
    private int positionX, positionY;
    private int height, width;
    private Sprite sprite;
    private BlackBomb belong;

    public Explosion (BlackBomb bomb) {
        belong = bomb;
        positionX = belong.getPositionX();
        positionY = belong.getPositionY();
        height = 16;
        width = 16;
    }

    @Override
    public boolean isColliding(Entity b) {
        return false;
    }

    @Override
    public boolean isPlayerCollisionFriendly() {
        return false;
    }

    @Override
    public void draw() {

    }

    @Override
    public void removeFromScene() {

    }

    @Override
    public int getPositionX() {
        return 0;
    }

    @Override
    public int getPositionY() {
        return 0;
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return null;
    }
}
