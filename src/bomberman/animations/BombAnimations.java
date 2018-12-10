/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.animations;

import bomberman.Renderer;
import bomberman.constants.GlobalConstants;
import bomberman.entity.Entity;

import java.util.ArrayList;
import java.util.List;

import bomberman.entity.staticobjects.BlackBomb;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * @author Ashish
 */
public class BombAnimations {
    Sprite blackBomb;
    double playSpeed;
    Entity entity;

    public Sprite getBlackBomb() {
        return blackBomb;
    }

    public void setBlackBomb(Sprite blackBomb) {
        this.blackBomb = blackBomb;
    }

    //取得爆炸動畫
    public Sprite getSprite(BlackBomb.STATE state) {
        if (state.equals(BlackBomb.STATE.EXPLODING)) {
            blackBomb = new Sprite(entity, 30, 0.1, 0, 0, 3, GlobalConstants.PLAYER_WIDTH, GlobalConstants.PLAYER_HEIGHT, 2, false);
        }

        return blackBomb;
    }

    public BombAnimations(Entity e) {
        entity = e;
        Image img = Renderer.getSpiteSheet();
        playSpeed = 0.3;

        List<Rectangle> specs = new ArrayList<>();

        specs.add(new Rectangle(181, 93, 17, 16));
        specs.add(new Rectangle(211, 93, 16, 16));
        specs.add(new Rectangle(240, 93, 18, 17));
        blackBomb = new Sprite(e, 30, playSpeed, img, specs, GlobalConstants.PLAYER_WIDTH + 2, GlobalConstants.PLAYER_HEIGHT + 2, 2, false);
    }
}
