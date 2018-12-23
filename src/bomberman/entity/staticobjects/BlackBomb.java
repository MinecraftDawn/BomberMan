/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.entity.staticobjects;

import bomberman.Renderer;
import bomberman.constants.GlobalConstants;
import bomberman.entity.player.Player;
import bomberman.animations.BombAnimations;
import bomberman.animations.Sprite;
import bomberman.entity.Entity;
import bomberman.entity.StaticEntity;
import bomberman.entity.boundedbox.RectBoundedBox;

import java.util.Date;

/**
 * @author Ashish
 */
public class BlackBomb implements StaticEntity {
    public int positionX = 0;
    public int positionY = 0;
    private int height;
    private int width;
    private Sprite sprite;
    private Player belong;

    RectBoundedBox entityBoundary;
    BombAnimations bomb_animations;
    Date addedDate;
    int bomberExistTime = 2000;
    STATE bombState;

    public enum STATE {
        INACTIVE,   //INACTIVE when bomb's timer hasnt yet started
        ACTIVE,     //Active when bomb's timer has started and it will explode soon
        EXPLODING,  //when bomb is exploding
        DEAD;   //when the bomb has already exploded
    }

    public BlackBomb(int x, int y, Player p) {
        belong = p;
        positionX = x;
        positionY = y;
        width = 16;
        height = 16;
        bomb_animations = new BombAnimations(this);
        sprite = bomb_animations.getBlackBomb();
        entityBoundary = new RectBoundedBox(positionX, positionY, width, height);
        addedDate = new Date();
        bombState = STATE.ACTIVE;
    }

    public boolean isAlive() {
        STATE s = checkBombState();
        if (s == STATE.DEAD) {
            return false;
        } else {
            if (s == STATE.ACTIVE || s == STATE.INACTIVE) {
                return true;
            }
            return true;
        }
    }

    //確認爆炸狀態
    public STATE checkBombState() {
        if (new Date().getTime() > bomberExistTime + addedDate.getTime()) {
            return STATE.DEAD;

        //若準備爆炸，則修改貼圖
        } else {
            return STATE.ACTIVE;
        }
    }

    public Player getBelong() {
        return belong;
    }

    @Override
    public boolean isColliding(Entity b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw() {
        Renderer.playAnimation(sprite);
    }

    @Override
    public void removeFromScene() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        return entityBoundary;
    }

    @Override
    public boolean isPlayerCollisionFriendly() {
        return true;
    }

}
