package entity;

import engine.Cooldown;
import engine.DrawManager;
import screen.GameScreen;
import engine.Core;

import java.awt.*;
import java.util.Set;

public class Item extends Entity {

    private static float savespeed = Ship.getSpeed();
    //private int savecooldown = (int) (Ship.getshootingCooldown().getduration() * 0.5);
    private static int col = 0;
    private static int row = 0;
    private int speed;

    /**
     * Constructor, establishes the entity's generic properties.
     *
     * @param positionX Initial position of the entity in the X axis.
     * @param positionY Initial position of the entity in the Y axis.
     * @param width     Width of the entity.
     * @param height    Height of the entity.
     * @param color
     */
    public Item(int positionX, int positionY, int speed) {
        super(positionX, positionY, 3 * 2, 5 * 2, Color.WHITE);

        this.speed = speed;

        setSprite();
    }

    public final void setSprite() {
        int random = (int) (Math.random() * 999);
        if (random < 500) this.spriteType = DrawManager.SpriteType.PositiveItems;
        else this.spriteType = DrawManager.SpriteType.NegativeItems;
    }

    public final void update() {
        this.positionY += this.speed;
    }

    public static void speedUp() {
        Ship.setSpeed((float) (Ship.getSpeed() * 1.5));
        //Ship.getshootingCooldown().setduration(Ship.getshootingCooldown().getduration() + savecooldown);
    }

    public static void speedUpClear() {
        Ship.setSpeed((float) (Ship.getSpeed() * 2 / 3));
    }

    public static void speedDown() {
        Ship.setSpeed((float) (Ship.getSpeed() * 0.5));
        //Ship.getshootingCooldown().setduration(Ship.getshootingCooldown().getduration() - savecooldown);
    }

    public static void speedDownClear() {
        Ship.setSpeed((float) (Ship.getSpeed() * 2.0));
    }

    public static void snare() {
        Ship.setSpeed(0);
    }

    public static void snareClear() {
        Ship.setSpeed(savespeed);
    }

    public static void bonusLife() {
        if (GameScreen.getlives() != Core.getMAX_LIVES()) {
            GameScreen.setlives(GameScreen.getlives() + 1);
        }
    }

    public static void bomb() {
        EnemyShipFormation.setisbomb(true);
    }

    public static void headshot() {

    }

    public static void setCol(int setcol) {col = setcol;}

    public static void setRow(int setrow) {row = setrow;}

    public final void setSpeed(final int speed) {
        this.speed = speed;
    }

    public final int getSpeed() {
        return this.speed;
    }
}