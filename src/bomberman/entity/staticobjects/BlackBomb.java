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
import bomberman.scenes.Sandbox;

import java.util.Date;
import java.util.Iterator;

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
    private boolean canPenetrate = true;

    RectBoundedBox entityBoundary;
    BombAnimations bomb_animations;
    long startTime;
    int bomberExistTime = 1800;
    int penetrateTime = 200;
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
        startTime = new Date().getTime();
        bombState = STATE.ACTIVE;
    }

    public void setBombState(STATE bombState) {
        this.bombState = bombState;
    }

    public boolean isExploded() {
        return (bombState == STATE.DEAD)? true:false;
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
        if (new Date().getTime() > bomberExistTime + startTime) {
            return STATE.DEAD;
        }
//        else if (new Date().getTime() > penetrateTime + startTime) {
//            canPenetrate = false;
////            Iterator<Player> it = Sandbox.playerList.iterator();
////            while (it.hasNext()) {
////                Player p = it.next();
////                if (positionX == p.positionX && positionY == p.positionY) {
////                    canPenetrate = false;
////                    break;
////                }
////                else canPenetrate = true;
////            }
//            return STATE.ACTIVE;
//        }
        else {
            return STATE.ACTIVE;
        }
    }

    public void determinePenetrate() {
        Iterator<Player> it = Sandbox.playerList.iterator();
        while (it.hasNext()) {
            Player p = it.next();
            int posX = p.getPositionX() + 16;
            int posY = p.getPositionY() + 16;

            posX = posX - posX % 32;
            posY = posY - posY % 32;

            if (positionX == posX && positionY == posY) {
                canPenetrate = true;
                break;
            }
            else {
                canPenetrate = false;
            }
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
        return canPenetrate;
    }

}
