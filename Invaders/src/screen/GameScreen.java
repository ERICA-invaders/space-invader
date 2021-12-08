package screen;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import engine.*;
import engine.DrawManager.SpriteType;
import entity.*;

/**
 * Implements the game screen, where the action happens.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 */
public class GameScreen extends Screen {

    /**
     * Milliseconds until the screen accepts user input.
     */
    private static final int INPUT_DELAY = 6000;
    /**
     * Bonus score for each life remaining at the end of the level.
     */
    private static final int LIFE_SCORE = 100;
    /**
     * Minimum time between bonus ship's appearances.
     */
    private static final int BONUS_SHIP_INTERVAL = 20000;
    /**
     * Maximum variance in the time between bonus ship's appearances.
     */
    private static final int BONUS_SHIP_VARIANCE = 10000;
    /**
     * Time until bonus ship explosion disappears.
     */
    private static final int BONUS_SHIP_EXPLOSION = 500;
    /**
     * Time from finishing the level to screen change.
     */
    private static final int SCREEN_CHANGE_INTERVAL = 1500;
    /**
     * Height of the interface separation line.
     */
    private static final int SEPARATION_LINE_HEIGHT = 40;

    /**
     * Current game difficulty settings.
     */
    private GameSettings gameSettings;
    /**
     * Current difficulty level number.
     */
    private int level;
    /**
     * Formation of enemy ships.
     */
    private EnemyShipFormation enemyShipFormation;
    /**
     * Player's ship.
     */
    private Ship ship;
    /**
     * Bonus enemy ship that appears sometimes.
     */
    private EnemyShip enemyShipSpecial;
    /**
     * Boss enemy ship that appears in level 8.
     */
    private EnemyShip bossShip;
    /**
     * Minimum time between bonus ship appearances.
     */
    private Cooldown enemyShipSpecialCooldown;
    /**
     * Time until bonus ship explosion disappears.
     */
    private Cooldown enemyShipSpecialExplosionCooldown;
    /**
     * Minimum time between boss ship appearances.
     */
    private Cooldown bossShipCooldown;
    /**
     * Time until boss ship explosion disappears.
     */
    private Cooldown bossShipExplosionCooldown;
    /**
     * Time until next wave comes.
     */
    private Cooldown bossShipPatternCooldown;
    /**
     * Boss's move Direction.
     */
    private int bossDirection = 2;
    /**
     * For boss pattern cooldown init
     */
    private boolean first = true;
    /**
     * Time from finishing the level to screen change.
     */
    private Cooldown screenFinishedCooldown;
    /**
     * Set of all bullets fired by on screen ships.
     */
    private Set<Bullet> bullets;
    /**
     * Current score.
     */
    private static int score;
    /**
     * Player lives left.
     */
    private static int lives;
    /**
     * Total bullets shot by the player.
     */
    private int bulletsShot;
    /**
     * Total ships destroyed by the player.
     */
    private static int shipsDestroyed;
    /**
     * Moment the game starts.
     */
    private long gameStartTime;
    /**
     * Checks if the level is finished.
     */
    private boolean levelFinished;
    /**
     * Checks if a bonus life is received.
     */
    private boolean bonusLife;

    private static boolean pause = false;
    private static final int SELECTION_TIME = 200;
    private Cooldown selectionCooldown;
    private long save = 0;
    private static int option = 2;
    private Set<Item> items;
    private static int headShot = 0;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param gameState    Current game state.
     * @param gameSettings Current game settings.
     * @param bonusLife    Checks if a bonus life is awarded this level.
     * @param width        Screen width.
     * @param height       Screen height.
     * @param fps          Frames per second, frame rate at which the game is run.
     */
    public GameScreen(final GameState gameState,
                      final GameSettings gameSettings, final boolean bonusLife,
                      final int width, final int height, final int fps) {
        super(width, height, fps);

        this.gameSettings = gameSettings;
        this.bonusLife = bonusLife;
        this.level = gameState.getLevel();
        this.score = gameState.getScore();
        this.lives = gameState.getLivesRemaining();
        if (this.bonusLife)
            this.lives++;
        this.bulletsShot = gameState.getBulletsShot();
        this.shipsDestroyed = gameState.getShipsDestroyed();

        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
    }

    /**
     * Initializes basic screen properties, and adds necessary elements.
     */
    public final void initialize() {
        super.initialize();

        if (this.level != 8) {
            enemyShipFormation = new EnemyShipFormation(this.gameSettings);
        } else {
            enemyShipFormation = new EnemyShipFormation(this.gameSettings, 200);
            this.bossShip = new EnemyShip(SpriteType.BossShip1);
            this.logger.info("Warning! Boss Appeared!");

        }
        enemyShipFormation.attach(this);
        this.ship = new Ship(this.width / 2, this.height - 30);
        // Appears each 10-30 seconds.
        this.enemyShipSpecialCooldown = Core.getVariableCooldown(
                BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
        this.enemyShipSpecialCooldown.reset();
        this.enemyShipSpecialExplosionCooldown = Core
                .getCooldown(BONUS_SHIP_EXPLOSION);
        this.bossShipExplosionCooldown = Core.getCooldown(BONUS_SHIP_EXPLOSION);
        this.bossShipPatternCooldown = Core.getCooldown(10000);
        this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
        this.bullets = new HashSet<Bullet>();
        this.items = new HashSet<Item>();

        // Special input delay / countdown.
        this.gameStartTime = System.currentTimeMillis();
        this.inputDelay = Core.getCooldown(INPUT_DELAY);
        this.inputDelay.reset();

    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        this.score += LIFE_SCORE * (this.lives - 1);
        this.logger.info("Screen cleared with a score of " + this.score);

        return this.returnCode;
    }

    private void nextMenuItem() {
        if (this.option == 3)
            this.option = 0;
        else if (this.option == 0)
            this.option = 2;
        else
            this.option++;
    }

    private void previousMenuItem() {
        if (this.option == 0)
            this.option = 3;
        else if (this.option == 2)
            this.option = 0;
        else
            this.option--;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
        super.update();

        if (this.inputDelay.checkFinished() && !this.levelFinished && !pause) {

            if (!this.ship.isDestroyed()) {
                boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                        || inputManager.isKeyDown(KeyEvent.VK_D);
                boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT)
                        || inputManager.isKeyDown(KeyEvent.VK_A);

                boolean isRightBorder = this.ship.getPositionX()
                        + this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
                boolean isLeftBorder = this.ship.getPositionX()
                        - this.ship.getSpeed() < 1;

                if (moveRight && !isRightBorder) {
                    this.ship.moveRight();
                }
                if (moveLeft && !isLeftBorder) {
                    this.ship.moveLeft();
                }
                if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
                    if (this.ship.shoot(this.bullets)) {
                        this.bulletsShot++;
                        if (headShot > 0) headShot--;
                    }
            }

            if (this.enemyShipSpecial != null) {
                if (!this.enemyShipSpecial.isDestroyed())
                    this.enemyShipSpecial.move(2, 0);
                else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
                    this.enemyShipSpecial = null;

            }
            if (this.enemyShipSpecial == null
                    && this.enemyShipSpecialCooldown.checkFinished()) {
                this.enemyShipSpecial = new EnemyShip();
                this.enemyShipSpecialCooldown.reset();
                this.logger.info("A special ship appears");
            }
            if (this.enemyShipSpecial != null
                    && this.enemyShipSpecial.getPositionX() > this.width) {
                this.enemyShipSpecial = null;
                this.logger.info("The special ship has escaped");
            }

            if (this.bossShip != null) {
                if (!this.bossShip.isDestroyed())
                    this.bossShip.move(bossDirection, 0);
                else if (this.bossShipExplosionCooldown.checkFinished())
                    this.bossShip = null;
            }

            if (this.bossShip != null && (this.bossShip.getPositionX() > this.width
                    || this.bossShip.getPositionX() < -37)) {
                bossDirection *= -1;
            }

            this.ship.update();
            this.enemyShipFormation.update();
            if (!this.enemyShipFormation.isEmpty()) {
                this.enemyShipFormation.shoot(this.bullets);
            }
            if (this.enemyShipFormation.isEmpty()) {
                if (first) {
                    bossShipPatternCooldown.reset();
                    first = false;
                }
                if (bossShipPatternCooldown.checkFinished()) {
                    enemyShipFormation = new EnemyShipFormation(this.gameSettings, 200);
                    enemyShipFormation.attach(this);
                    bossShipPatternCooldown.reset();
                }
            }
            if (inputManager.isKeyDown(KeyEvent.VK_P)) {
                pause = true;
                save = System.currentTimeMillis();
            }
        }

        manageCollisions();
        if (!pause) {
            cleanBullets();
            cleanItems();
        }
        draw();

        if (this.level != 8) {
            if ((this.enemyShipFormation.isEmpty() || this.lives == 0)
                    && !this.levelFinished) {
                this.levelFinished = true;
                this.screenFinishedCooldown.reset();
            }
        } else {
            if ((this.bossShip.isDestroyed() || this.lives == 0)
                    && !this.levelFinished) {
                this.levelFinished = true;
                this.screenFinishedCooldown.reset();
            }
        }

        if (this.levelFinished && this.screenFinishedCooldown.checkFinished())
            this.isRunning = false;

        if (pause) {
            if (this.selectionCooldown.checkFinished()
                    && this.inputDelay.checkFinished()) {
                if (inputManager.isKeyDown(KeyEvent.VK_UP)
                        || inputManager.isKeyDown(KeyEvent.VK_W)) {
                    previousMenuItem();
                    this.selectionCooldown.reset();
                }
                if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                        || inputManager.isKeyDown(KeyEvent.VK_S)) {
                    nextMenuItem();
                    this.selectionCooldown.reset();
                }
                if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                    if (option == 0) {
                        // main menu
                        level = Core.getNUM_LEVELS();
                        pause = false;
                        this.isRunning = false;
                        this.option = 2;
                    } else if (option == 2) {
                        // resume
                        this.enemyShipFormation.getShootingCooldown().setTime(this.enemyShipFormation.getShootingCooldown().getTime() + System.currentTimeMillis() - save);
                        this.ship.getShootingCooldown().setTime(this.ship.getShootingCooldown().getTime() + System.currentTimeMillis() - save);
                        pause = false;
                    } else if (option == 3) {
                        // new game
                        level = 0;
                        lives = Core.getMAX_LIVES();
                        bulletsShot = 0;
                        shipsDestroyed = 0;
                        pause = false;
                        this.isRunning = false;
                        this.option = 2;
                        score = -LIFE_SCORE * (Core.getMAX_LIVES() - 1);
                    }
                }
            }
        }

    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawEntity(this.ship, this.ship.getPositionX(),
                this.ship.getPositionY());
        if (this.enemyShipSpecial != null)
            drawManager.drawEntity(this.enemyShipSpecial,
                    this.enemyShipSpecial.getPositionX(),
                    this.enemyShipSpecial.getPositionY());

        if (this.bossShip != null)
            drawManager.drawEntity(this.bossShip,
                    this.bossShip.getPositionX(),
                    this.bossShip.getPositionY());

        enemyShipFormation.draw();

        for (Bullet bullet : this.bullets)
            drawManager.drawEntity(bullet, bullet.getPositionX(),
                    bullet.getPositionY());

        for (Item item : this.items)
            drawManager.drawEntity(item, item.getPositionX(),
                    item.getPositionY());

        // Interface.
        drawManager.drawScore(this, this.score);
        drawManager.drawLives(this, this.lives);
        drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);

        if (pause) drawManager.drawPauseMenu(this, option);

        // Countdown to game start.
        if (!this.inputDelay.checkFinished()) {
            int countdown = (int) ((INPUT_DELAY
                    - (System.currentTimeMillis()
                    - this.gameStartTime)) / 1000);
            drawManager.drawCountDown(this, this.level, countdown,
                    this.bonusLife);
            drawManager.drawHorizontalLine(this, this.height / 2 - this.height
                    / 12);
            drawManager.drawHorizontalLine(this, this.height / 2 + this.height
                    / 12);
        }

        drawManager.completeDrawing(this);
    }

    /**
     * Cleans bullets that go off screen.
     */
    private void cleanBullets() {
        Set<Bullet> recyclable = new HashSet<Bullet>();
        for (Bullet bullet : this.bullets) {
            bullet.update();
            if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
                    || bullet.getPositionY() > this.height)
                recyclable.add(bullet);
        }
        this.bullets.removeAll(recyclable);
        BulletPool.recycle(recyclable);
    }

    private void cleanItems() {
        Set<Item> recyclable = new HashSet<>();
        for (Item item : this.items) {
            item.update();
            if (item.getPositionY() < SEPARATION_LINE_HEIGHT
                    || item.getPositionY() > this.height)
                recyclable.add(item);
        }
        this.items.removeAll(recyclable);
        ItemPool.recycle(recyclable);
    }

    /**
     * Manages collisions between bullets and ships.
     */
    private void manageCollisions() {
        Set<Bullet> recyclable = new HashSet<Bullet>();
        Set<Item> recyclableItem = new HashSet<Item>();
        for (Bullet bullet : this.bullets)
            if (bullet.getSpeed() > 0) {
                if (checkCollision(bullet, this.ship) && !this.levelFinished) {
                    recyclable.add(bullet);
                    if (!this.ship.isDestroyed()) {
                        this.ship.destroy();
                        this.lives--;
                        this.logger.info("Hit on player ship, " + this.lives
                                + " lives remaining.");
                    }
                }
            } else {
                for (EnemyShip enemyShip : this.enemyShipFormation)
                    if (!enemyShip.isDestroyed()
                            && checkCollision(bullet, enemyShip)) {
                        if (this.enemyShipFormation.destroy(enemyShip)) {
                            this.shipsDestroyed++;
                            this.score += enemyShip.getPointValue();
                            int random = (int) (Math.random() * 999);
                            if (random < 1000) enemyShip.drop(items, enemyShip.getPositionX(), enemyShip.getPositionY());
                        }
                        recyclable.add(bullet);
                    }
                if (this.enemyShipSpecial != null
                        && !this.enemyShipSpecial.isDestroyed()
                        && checkCollision(bullet, this.enemyShipSpecial)) {
                    this.score += this.enemyShipSpecial.getPointValue();
                    this.shipsDestroyed++;
                    this.enemyShipSpecial.destroy();
                    this.enemyShipSpecialExplosionCooldown.reset();
                    recyclable.add(bullet);
                }
                if (this.bossShip != null
                        && !this.bossShip.isDestroyed()
                        && checkCollision(bullet, this.bossShip)) {
                    this.score += this.bossShip.getPointValue();
                    this.shipsDestroyed++;
                    this.bossShip.destroy();
                    this.bossShipExplosionCooldown.reset();
                    recyclable.add(bullet);
                }
            }
        for (Item item : this.items) {
            if (checkCollisionItem(item, this.ship) && !this.levelFinished) {
                this.logger.info("item get");
                recyclableItem.add(item);
                if (!this.ship.isDestroyed()) {
                    int random = (int)(Math.random() * 999);
                    if (item.getSpriteType() == SpriteType.NegativeItems) {
                        if (random < 500) {
                            speedDown();
                        }
                        else item.snare();
                    } else {
                        if (random < 250) {
                            speedUp();
                        }
                        else if (random < 500) item.bonusLife();
                        else if (random < 750) item.bomb();
                        else item.headshot();
                    }
                }
            }
        }
        this.bullets.removeAll(recyclable);
        BulletPool.recycle(recyclable);
        this.items.removeAll(recyclableItem);
        ItemPool.recycle(recyclableItem);
    }

    /**
     * Checks if two entities are colliding.
     *
     * @param a First entity, the bullet.
     * @param b Second entity, the ship.
     * @return Result of the collision test.
     */
    private boolean checkCollisionItem (final  Entity a, final  Entity b) {
        // Calculate center point of the entities in both axis.
        int centerAX = a.getPositionX() + a.getWidth() / 3 + 30;
        int centerAY = a.getPositionY() + a.getHeight() / 2 + 10;
        int centerBX = b.getPositionX() + b.getWidth() / 2;
        int centerBY = b.getPositionY() + b.getHeight() / 2;
        // Calculate maximum distance without collision.
        int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
        int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
        // Calculates distance.
        int distanceX = Math.abs(centerAX - centerBX);
        int distanceY = Math.abs(centerAY - centerBY);

        return distanceX < maxDistanceX && distanceY < maxDistanceY;
    }

    private boolean checkCollision(final Entity a, final Entity b) {
        // Calculate center point of the entities in both axis.
        int centerAX = a.getPositionX() + a.getWidth() / 3 - 10;
        int centerAY = a.getPositionY() + a.getHeight() / 2;
        int centerBX = b.getPositionX() + b.getWidth() / 2;
        int centerBY = b.getPositionY() + b.getHeight() / 2;
        // Calculate maximum distance without collision.
        int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
        int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
        // Calculates distance.
        int distanceX = Math.abs(centerAX - centerBX);
        int distanceY = Math.abs(centerAY - centerBY);

        return distanceX < maxDistanceX && distanceY < maxDistanceY;
    }

    /**
     * Returns a GameState object representing the status of the game.
     *
     * @return Current game state.
     */
    public final GameState getGameState() {
        return new GameState(this.level, this.score, this.lives,
                this.bulletsShot, this.shipsDestroyed);
    }

    public final int getOption() {
        return this.option;
    }

    public static final int getlives() {
        return lives;
    }

    public static final void setlives(int setlives) {
        lives = setlives;
    }

    public static final int getScore() {
        return score;
    }

    public static final void setScore(int setscore) {
        score = setscore;
    }

    public static final int getShipsDestroyed() {
        return shipsDestroyed;
    }

    public static final void setShipsDestroyed(int setshipdestroyed) {
        shipsDestroyed = setshipdestroyed;
    }

    public static final void setHeadShot(int setheadshot) {
        headShot = setheadshot;
    }

    public static final int getHeadShot() {
        return headShot;
    }

    Timer timer;

    public class SpeedUpClear extends TimerTask {
        public void run() {
            logger.info("speedUpClear");
            ship.setSpeed((ship.getSpeed() * 2.0f / 3.0f));
            ship.getShootingCooldown().setduration((int)(ship.getShootingCooldown().getduration() * 1.5));
            timer.cancel();
        }
    }

    public class SpeedDownClear extends TimerTask {
        public void run() {
            logger.info("speedDownClear");
            ship.setSpeed((ship.getSpeed() * 2.0f));
            ship.getShootingCooldown().setduration((int)(ship.getShootingCooldown().getduration() * 2.0 / 3));
            timer.cancel();
        }
    }

    public final void speedUp() {
        timer = new Timer();
        logger.info("speedUp");
        this.ship.setSpeed((this.ship.getSpeed() * 1.5f));
        this.ship.getShootingCooldown().setduration((int)(this.ship.getShootingCooldown().getduration() * 2.0 / 3));
        timer.schedule(new SpeedUpClear(), 10000);
    }

    public final void speedDown() {
        timer = new Timer();
        logger.info("speedDown");
        this.ship.setSpeed((this.ship.getSpeed() * 0.5f));
        this.ship.getShootingCooldown().setduration((int)(this.ship.getShootingCooldown().getduration() * 1.5));
        timer.schedule(new SpeedDownClear(), 10000);
    }
}