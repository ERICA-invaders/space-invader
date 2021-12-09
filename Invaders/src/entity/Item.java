package entity;

import engine.DrawManager;
import screen.GameScreen;
import engine.Core;
import entity.Ship;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class Item extends Entity {

    private static float savespeed = Ship.getSpeed();
    private static int col = 0;
    private static int row = 0;
    private int speed;

    private static Logger logger;

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
        logger = Core.getLogger();

        setSprite();
    }

    public final void setSprite() {
        int random = (int) (Math.random() * 900);
        if (random < 600) this.spriteType = DrawManager.SpriteType.PositiveItems;
        else this.spriteType = DrawManager.SpriteType.NegativeItems;
    }

    public final void update() {
        this.positionY += this.speed;
    }

    static Timer timer;

    public static class SnareClear extends TimerTask {
        public void run() {
            Item.logger.info("snareClear");
            Ship.setSpeed(savespeed);
            timer.cancel();
        }
    }

    public static void snare() {
        timer = new Timer();
        Item.logger.info("snare");
        Ship.setSpeed(0);
        timer.schedule(new SnareClear(), 5000);
    }

    public static void bonusLife() {
        if (GameScreen.getlives() != Core.getMAX_LIVES()) {
            GameScreen.setlives(GameScreen.getlives() + 1);
            Item.logger.info("getBonusLife");
        }
        Item.logger.info("bonusLife");
    }

//    public static void bomb() {
//        //EnemyShipFormation.setisbomb(true);
//        Item.logger.info("bomb");
//    }

    public static void headshot() {
        GameScreen.setHeadShot(4);
        Item.logger.info("headshot");
    }

    public final void setSpeed(final int speed) {
        this.speed = speed;
    }

    public final int getSpeed() {
        return this.speed;
    }

    public static final void nomalSpeed() {
        Ship.setSpeed(savespeed);
    }

    public final void setSpriteType(DrawManager.SpriteType setspritetype) {
        this.spriteType = setspritetype;
    }
}