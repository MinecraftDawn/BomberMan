/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.gamecontroller;

import java.util.List;

import bomberman.constants.Direction;
import bomberman.entity.Entity;
import bomberman.entity.player.Player;
import bomberman.entity.staticobjects.BlackBomb;
import bomberman.scenes.Sandbox;

import java.util.Iterator;
import java.util.Vector;

import javafx.scene.input.KeyCode;

/**
 * @author Ashish
 */
public class InputManager {
    //need to be overloaded
    public static void handlePlayerMovements() {
        List keyboardInputs = EventHandler.getInputList();
        Player player1 = Sandbox.getPlayer1();
        Player player2 = Sandbox.getPlayer2();

        //System.err.println(""+keyboardInputs);
        if (keyboardInputs.contains(KeyCode.UP)) {
            player1.move(player1.getSpeed(), Direction.UP);
        }
        if (keyboardInputs.contains(KeyCode.DOWN)) {
            player1.move(player1.getSpeed(), Direction.DOWN);
        }
        if (keyboardInputs.contains(KeyCode.LEFT)) {
            player1.move(player1.getSpeed(), Direction.LEFT);
        }
        if (keyboardInputs.contains(KeyCode.RIGHT)) {
            player1.move(player1.getSpeed(), Direction.RIGHT);
        }
        if (keyboardInputs.contains(KeyCode.W)) {
            player2.move(player2.getSpeed(), Direction.UP);
        }
        if (keyboardInputs.contains(KeyCode.S)) {
            player2.move(player2.getSpeed(), Direction.DOWN);
        }
        if (keyboardInputs.contains(KeyCode.A)) {
            player2.move(player2.getSpeed(), Direction.LEFT);
        }
        if (keyboardInputs.contains(KeyCode.D)) {
            player2.move(player2.getSpeed(), Direction.RIGHT);
        }
        if (!keyboardInputs.contains(KeyCode.LEFT) &&
                !keyboardInputs.contains(KeyCode.RIGHT) &&
                !keyboardInputs.contains(KeyCode.UP) &&
                !keyboardInputs.contains(KeyCode.DOWN) &&
                !keyboardInputs.contains(KeyCode.W) &&
                !keyboardInputs.contains(KeyCode.A) &&
                !keyboardInputs.contains(KeyCode.S) &&
                !keyboardInputs.contains(KeyCode.D)) {
            player1.move(0, Direction.DOWN);
            player2.move(0, Direction.DOWN);
        }

        //Drop bomb
        if (keyboardInputs.contains(KeyCode.ENTER) || keyboardInputs.contains(KeyCode.SPACE)) {
            //須創建Boomb的EntityList
            //需要判斷該位置是否有炸彈
            Player p = (keyboardInputs.contains(KeyCode.ENTER)) ? player1 : player2;
            int posX, posY;

            posX = p.getPositionX() + 16;
            posY = p.getPositionY() + 16;

            posX = posX - posX % 32;
            posY = posY - posY % 32;

            //在低能一點忘記改這裡啊><
            //爽Ra
            BlackBomb bomb = new BlackBomb(posX, posY, p);
            Sandbox.addEntityToGame(bomb);
//            Sandbox.bombList.add(bomb);
        }
    }
}
