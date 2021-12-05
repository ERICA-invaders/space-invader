package entity;

import engine.Cooldown;
import screen.GameScreen;
import engine.Core;

public class Item {

    private static float savespeed = Ship.getSpeed();
    //private int savecooldown = (int) (Ship.getshootingCooldown().getduration() * 0.5);
    private static int col = 0;
    private static int row = 0;

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
}