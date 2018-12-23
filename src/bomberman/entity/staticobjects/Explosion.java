package bomberman.entity.staticobjects;

import bomberman.Renderer;
import bomberman.animations.Sprite;
import bomberman.entity.Entity;
import bomberman.entity.boundedbox.RectBoundedBox;
import bomberman.animations.ExplosionAnimation;

import java.util.Date;

public class Explosion implements Entity {
    private int positionX, positionY;
    private int height, width;
    private Sprite sprite;
    private BlackBomb belong;
    private long explodeTime = 200;
    private long startTime;
    private ExplosionAnimation animation;

    public Explosion (BlackBomb bomb) {
        belong = bomb;
        positionX = belong.getPositionX();
        positionY = belong.getPositionY();
        height = 16;
        width = 16;
        animation = new ExplosionAnimation(this);
        startTime = new Date().getTime();
        sprite = animation.getExplosion();
    }

    public enum STATE{
        EXPLODING,
        EXPLOD_END
    }

    public STATE getState(){
        if(new Date().getTime() > startTime){
            return STATE.EXPLOD_END;
        }else{
            return STATE.EXPLODING;
        }
    }

    public BlackBomb getBelong() {
        return belong;
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
        Renderer.playAnimation(sprite);
    }

    @Override
    public void removeFromScene() {

    }

    @Override
    public int getPositionX() {
        return positionX;
    }

    @Override
    public int getPositionY() {
        return positionY;
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return null;
    }
}
