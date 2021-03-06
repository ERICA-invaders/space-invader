package entity;

import java.awt.Color;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;
import engine.SoundManager;
import screen.GameScreen;

/**
 * Implements a enemy ship, to be destroyed by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class EnemyShip extends Entity {
	
	/** Point value of a type A enemy. */
	private static final int A_TYPE_POINTS = 10;
	/** Point value of a type B enemy. */
	private static final int B_TYPE_POINTS = 20;
	/** Point value of a type C enemy. */
	private static final int C_TYPE_POINTS = 30;
	/** Point value of a bonus enemy. */
	private static final int BONUS_TYPE_POINTS = 100;
	/** Point value of a type Boss enemy. */
	private static final int BOSS_POINTS = 1000;

	/** Cooldown between sprite changes. */
	private Cooldown animationCooldown;
	/** Checks if the ship has been hit by a bullet. */
	private boolean isDestroyed;
	/** Values of the ship, in points, when destroyed. */
	private int pointValue;

	private int life;
	private int maxLife;
	private static final int ITEM_SPEED = 3;

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 * @param spriteType
	 *            Sprite type, image corresponding to the ship.
	 */
	public EnemyShip(final int positionX, final int positionY,
			final SpriteType spriteType) {
		super(positionX, positionY, 12 * 2, 8 * 2, Color.WHITE);

		this.spriteType = spriteType;
		this.animationCooldown = Core.getCooldown(500);
		this.isDestroyed = false;

		this.life = this.maxLife = 3;

		switch (this.spriteType) {
		case EnemyShipA1:
		case EnemyShipA2:
			this.pointValue = A_TYPE_POINTS;
			break;
		case EnemyShipB1:
		case EnemyShipB2:
			this.pointValue = B_TYPE_POINTS;
			break;
		case EnemyShipC1:
		case EnemyShipC2:
			this.pointValue = C_TYPE_POINTS;
			break;
		default:
			this.pointValue = 0;
			break;
		}
	}

	/**
	 * Constructor, establishes the ship's properties for a special ship, with
	 * known starting properties.
	 */
	public EnemyShip() {
		super(-32, 60, 16 * 2, 7 * 2, Color.RED);
		int random = (int)(Math.random() * 999);
		this.spriteType = SpriteType.EnemyShipSpecial;
		this.isDestroyed = false;
		this.life = this.maxLife = 1;
		if (random < 500) {
			this.pointValue = BONUS_TYPE_POINTS;
		} else {
			this.setColor(Color.GREEN);
		}
	}

	/**
	 * Constructor, establishes the ship's properties for a boss ship, with
	 * known starting properties.
	 */
	public EnemyShip(final SpriteType spriteType) {
		super(-37, 90, 16 * 3, 16 * 2, Color.RED);

		this.spriteType = SpriteType.BossShip1;
		this.isDestroyed = false;
		this.pointValue = BONUS_TYPE_POINTS;

		this.life = this.maxLife = 100;
	}

	/**
	 * Getter for the score bonus if this ship is destroyed.
	 * 
	 * @return Value of the ship.
	 */
	public final int getPointValue() {
		return this.pointValue;
	}

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

			switch (this.spriteType) {
			case EnemyShipA1:
				this.spriteType = SpriteType.EnemyShipA2;
				break;
			case EnemyShipA2:
				this.spriteType = SpriteType.EnemyShipA1;
				break;
			case EnemyShipB1:
				this.spriteType = SpriteType.EnemyShipB2;
				break;
			case EnemyShipB2:
				this.spriteType = SpriteType.EnemyShipB1;
				break;
			case EnemyShipC1:
				this.spriteType = SpriteType.EnemyShipC2;
				break;
			case EnemyShipC2:
				this.spriteType = SpriteType.EnemyShipC1;
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Destroys the ship, causing an explosion.
	 */
	public final boolean destroy() {
		if (GameScreen.getHeadShot() > 0) life -= 20;
		else life--;
		if(life > 0) {
			SoundManager.instance.playSFX(SoundManager.eSFX.eDamage);
			return false;
		} else {
			SoundManager.instance.playSFX(SoundManager.eSFX.eDie);
			this.isDestroyed = true;
			this.spriteType = SpriteType.Explosion;
			return true;
		}
	}

	public final void drop(final Set<Item> items, final int positionX, final int positionY) {
		items.add(ItemPool.getItem(positionX, positionY, ITEM_SPEED));
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
		return (byte) ((205d * this.life / this.maxLife) + 50);
	}
}
