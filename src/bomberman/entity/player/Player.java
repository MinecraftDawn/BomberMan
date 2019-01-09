package bomberman.entity.player;

import bomberman.GameLoop;
import bomberman.Renderer;
import bomberman.animations.PlayerAnimations;
import bomberman.animations.Sprite;
import bomberman.constants.Direction;
import bomberman.constants.GlobalConstants;
import bomberman.entity.Entity;
import bomberman.entity.KillableEntity;
import bomberman.entity.MovingEntity;
import bomberman.entity.boundedbox.RectBoundedBox;
import bomberman.entity.staticobjects.Explosion;
import bomberman.scenes.Sandbox;
import javafx.scene.input.KeyCode;

public class Player implements MovingEntity, KillableEntity {

    private int health;
    private boolean isAlive;
    private int bombLimit;
    private int playerNumber;
    RectBoundedBox playerBoundary;
    private int power;
    private int speed;

    Sprite currentSprite;
    PlayerAnimations playerAnimations;

    Direction currentDirection;

    public int positionX = 0;
    public int positionY = 0;

    String name;

    public Player(int playerNum) {
        playerNumber = playerNum;
        if (playerNumber == 1) {
            init(64, 64);
        } else if (playerNumber == 2) {
            init(576, 576);
        }
        power = 3;
        health = 100;
        isAlive = true;
        bombLimit = 0;
        speed = 3;
    }

    public Player(int posX, int posY) {
        init(posX, posY);
        health = 100;
        isAlive = true;
        bombLimit = 0;
    }

    private void init(int x, int y) {
        name = "Player" + playerNumber;

        playerAnimations = new PlayerAnimations(this);

        positionX = x;
        positionY = y;

        playerBoundary = new RectBoundedBox(positionX, positionY, GlobalConstants.PLAYER_WIDTH, GlobalConstants.PLAYER_HEIGHT);

        currentSprite = playerAnimations.getPlayerIdleSprite();
    }

    public void move(Direction direction) {
        move(1, direction);
    }

    private void setCurrentSprite(Sprite s) {
        if (s != null) {
            currentSprite = s;
        } else {
            System.out.println("Sprite missing!");
        }
    }

    public void damage(int damage){
        health -= damage;

        if(health <= 0 ){
            health = 0;
            isAlive = false;
        }
    }

    public int getPower(){
        return power;
    }

    public int getHealth() {
        return health;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public String toString() {
        return name;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    @Override
    public boolean isColliding(Entity b) {
        // playerBoundary.setPosition(positionX, positionY);
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) b.getBoundingBox();
        return playerBoundary.checkCollision(otherEntityBoundary);
    }

    @Override
    public void draw() {
        if (currentSprite != null) {
            Renderer.playAnimation(currentSprite);
        }
    }

    @Override
    public void die() {
        setCurrentSprite(playerAnimations.getPlayerDying());
    }

    private boolean checkCollisions(int x, int y) {
        playerBoundary.setPosition(x, y);

        for (Entity e : Sandbox.getEntities()) {

            if (e != this && isColliding(e) && !e.isPlayerCollisionFriendly()) {
                if (e instanceof Explosion) {
                    damage(100);
                }
                else
                    playerBoundary.setPosition(positionX, positionY);
                /*
                System.out.println("Player x="+getPositionX()+" y="
                        +getPositionY()+" colliding with x="+e.getPositionX()
                        +" y="+e.getPositionY());
                */
                return true;
            }
        }
        playerBoundary.setPosition(positionX, positionY);
        return false;
    }

    @Override
    public void move(int steps, Direction direction) {
        steps *= GameLoop.getDeltaTime();

        if (steps == 0) {
            setCurrentSprite(playerAnimations.getPlayerIdleSprite());
            return;
        } else {
            switch (direction) {
                case UP:
                    if (!checkCollisions(positionX, positionY - steps)) {
                        positionY -= steps;
                        setCurrentSprite(playerAnimations.getMoveUpSprite());
                        currentDirection = Direction.UP;
                    }
                    break;
                case DOWN:
                    if (!checkCollisions(positionX, positionY + steps)) {
                        positionY += steps;
                        setCurrentSprite(playerAnimations.getMoveDownSprite());
                        currentDirection = Direction.DOWN;
                    }
                    break;
                case LEFT:
                    if (!checkCollisions(positionX - steps, positionY)) {
                        positionX -= steps;
                        setCurrentSprite(playerAnimations.getMoveLeftSprite());
                        currentDirection = Direction.LEFT;
                    }
                    break;
                case RIGHT:
                    if (!checkCollisions(positionX + steps, positionY)) {
                        positionX += steps;
                        setCurrentSprite(playerAnimations.getMoveRightSprite());
                        currentDirection = Direction.RIGHT;
                    }
                    break;
                default:
                    setCurrentSprite(playerAnimations.getPlayerIdleSprite());
            }
        }
    }

    @Override
    public void reduceHealth(int damage) {
        if (health - damage <= 0) {
            die();
        } else {
            health -= damage;
        }
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
        playerBoundary.setPosition(positionX, positionY);
        return playerBoundary;
    }

    @Override
    public boolean isPlayerCollisionFriendly() {
        return true;
    }

    public int getLimit() {
        return bombLimit;
    }

    public void limitIncrease() {
        bombLimit++;
    }

    public void limitDecrease() {
        bombLimit--;
    }
}
