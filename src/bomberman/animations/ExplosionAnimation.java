package bomberman.animations;

import bomberman.Renderer;
import bomberman.constants.GlobalConstants;
import bomberman.entity.Entity;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExplosionAnimation {
    private Sprite explosion;
    private double playSpeed;
    //與BlackBomb的建構方式類似
    public ExplosionAnimation(Entity e) {
        Image img = Renderer.getSpiteSheet();
        playSpeed = 0.3;

        List<Rectangle> specs = new ArrayList<>();

        specs.add(new Rectangle(191, 153, 16, 16));
        specs.add(new Rectangle(151, 153, 16, 16));
        explosion = new Sprite(e, 30, playSpeed, img, specs, GlobalConstants.PLAYER_WIDTH + 2, GlobalConstants.PLAYER_HEIGHT + 2, 2, false);
    }

    public Sprite getExplosion() {
        return explosion;
    }
}
