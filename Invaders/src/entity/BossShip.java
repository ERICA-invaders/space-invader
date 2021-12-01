package entity;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

import java.awt.*;

/**
 * Implements a boss enemy ship, to be destroyed by the player.
 */
public class BossShip extends Entity {

    /** Point value of a type Boss enemy. */
    private static final int BOSS_POINTS = 1000;

    /** Cooldown between sprite changes. */
    private Cooldown animationCooldown;
    /** Checks if the Boss has been hit by a bullet. */
    private boolean isDestroyed;
    /** Values of the Boss, in points, when destroyed. */
    private int pointValue;

    private int life;
    private int maxLife;

    /**
     * Constructor, establishes the boss's generic properties.
     *
     * @param positionX Initial position of the entity in the X axis.
     * @param positionY Initial position of the entity in the Y axis.
     * @param spriteType Sprite type, image corresponding to the Boss.
     */
    public BossShip(final int positionX, final int positionY, final SpriteType spriteType) {
        super(positionX, positionY, 12 * 2, 8 * 2, Color.white);

        this.spriteType = spriteType;
        this.animationCooldown = Core.getCooldown(500);
        this.isDestroyed = false;

        this.life = this.maxLife = 30;

        this.pointValue = BOSS_POINTS;
    }

    /**
     * Getter for the score bonus if this Boss is destroyed.
     *
     * @return Value of the ship.
     */
    public final int getPointValue() { return this.pointValue; }

    /**
     * Moves the ship the specified distance.
     *
     * @param distanceX
     *            Distance to move in the X axis.
     * @param distanceY
     *            Distance to move in the Y axis.
     */
    public final void move(final int distanceX, final int distanceY) {
        this.positionX += distanceX;
        this.positionY += distanceY;
    }

    /**
     * Updates attributes, mainly used for animation purposes.
     */
    public final void update() {
        if (this.animationCooldown.checkFinished()) {
            this.animationCooldown.reset();


        }
    }

    /**
     * Destroys the ship, causing an explosion.
     */
    public final boolean destroy() {
        life--;
        if(life > 0) {
            return false;
        } else {
            this.isDestroyed = true;
            this.spriteType = SpriteType.Explosion;
            return true;
        }
    }

    /**
     * Checks if the ship has been destroyed.
     *
     * @return True if the ship has been destroyed.
     */
    public final boolean isDestroyed() {
        return this.isDestroyed;
    }

    public final byte getAlpha() {
        return (byte) (255f * this.life / this.maxLife);
    }
}
