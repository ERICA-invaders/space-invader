package entity;

import engine.Cooldown;

public class Item {

    private static float savespeed = (float) (Ship.getSpeed() * 0.5);
    private int savecooldown = (int) (Ship.getshootingCooldown().getduration() * 0.5);

    public static void speedUp() {
        Ship.setSpeed((float) (Ship.getSpeed() + savespeed));
        Ship.getshootingCooldown().setduration(Ship.getshootingCooldown().getduration() + savecooldown);
    }

    public static void speedDown() {
        Ship.setSpeed((float) (Ship.getSpeed() - savespeed));
        Ship.getshootingCooldown().setduration(Ship.getshootingCooldown().getduration() - savecooldown);
    }

    public static void stun() {

    }

    public static void stunClear() {

    }

    public static void bonusLife() {

    }

    public static void bomb() {

    }

    public static void headshot() {

    }
}
