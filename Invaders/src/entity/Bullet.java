package entity;

import java.awt.Color;

import engine.Cooldown;
import engine.DrawManager.SpriteType;
import screen.GameScreen;

/**
 * Implements a bullet that moves vertically up or down.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Bullet extends Entity {

	/**
	 * Speed of the bullet, positive or negative depending on direction -
	 * positive is down.
	 */
	private int speed;

	/** Cooldown between sprite changes. */
	private Cooldown animationCoolDown;
	/** Values of the bullet, in points, when destroyed. */
	private boolean isDestroyed;

	/**
	 * Constructor, establishes the bullet's properties.
	 * 
	 * @param positionX
	 *            Initial position of the bullet in the X axis.
	 * @param positionY
	 *            Initial position of the bullet in the Y axis.
	 * @param speed
	 *            Speed of the bullet, positive or negative depending on
	 *            direction - positive is down.
	 */
	public Bullet(final int positionX, final int positionY, final int speed) {
		super(positionX, positionY, 3 * 2, 5 * 2, Color.WHITE);

		this.speed = speed;

		setSprite();
	}

	/**
	 * Sets correct sprite for the bullet, based on speed.
	 */
	public final void setSprite() {
		if (speed < 0) {
			if (GameScreen.getHeadShot() > 0) this.spriteType = SpriteType.Bullet2;
			else this.spriteType = SpriteType.Bullet;
		}
		else
			this.spriteType = SpriteType.EnemyBullet;
	}

	/**
	 * Updates the bullet's position.
	 */
	public final void update() {
		this.positionY += this.speed;
		if (speed < 0) {
			if (GameScreen.getHeadShot() > 0) this.spriteType = SpriteType.Bullet2;
			else this.spriteType = SpriteType.Bullet1;
		}
		else
			this.spriteType = SpriteType.EnemyBullet1;
	}

	/**
	 * Setter of the speed of the bullet.
	 * 
	 * @param speed
	 *            New speed of the bullet.
	 */
	public final void setSpeed(final int speed) {
		this.speed = speed;
	}

	/**
	 * Getter for the speed of the bullet.
	 * 
	 * @return Speed of the bullet.
	 */
	public final int getSpeed() {
		return this.speed;
	}

	public final void setSpriteType(SpriteType spriteType) {this.spriteType = spriteType;}
}
