package fi.hbp.angr.logic;

import com.badlogic.gdx.math.MathUtils;

/**
 * Game state.
 */
public class GameState {
    /**
     * Grenade counter.
     */
    public class Grenades {
        /**
         * Amount of grenades at the beginning.
         */
        public final int originalCount;
        /**
         * Current count.
         */
        private int count;

        /**
         * Constructor for Grenades class.
         * @param amount Amount of grenades available.
         */
        public Grenades(int amount) {
            this.originalCount = amount;
            this.count = amount;
        }

        /**
         * Decrement amount of grenades by one.
         */
        public void decrement() {
            if (count > 0)
                count--;
        }

        /**
         * Returns current grenade count.
         * @return current grenade count.
         */
        public int getCount() {
            return count;
        }
    }

    /**
     * Game score.
     */
    private int score = 0;
    /**
     * Badge scaling.
     */
    private int badgeScale = 1;
    /**
     * Enemies left in this map.
     */
    private int enemyCount = 0;
    /**
     * Grenade counter.
     */
    private Grenades grenades = new Grenades(0);
    /**
     * All game stats calculated true/false.
     */
    private boolean gameFinalized = false;

    /**
     * Initialize game state object.
     * @param badgeScale points needed to achieve one badge.
     * @param enemies enemy count on the current level at the beginning of the game.
     * @param grenades amount of grenades available to clear the current level.
     */
    public void init(int badgeScale, int enemies, int grenades) {
        if (gameFinalized == true)
            return;

        this.badgeScale = (badgeScale > 0) ? badgeScale : 1;
        this.enemyCount = enemies;
        this.grenades = new Grenades(grenades);
    }

    /**
     * Add points achieved by the player.
     * @param value point added.
     * @param enemyDestroyed was enemy destroyed when points were achieved?
     */
    public void addPoints(int value, boolean enemyDestroyed) {
        if (gameFinalized)
            return;

        score += value;
        if (enemyDestroyed)
            enemyCount--;
    }

    /**
     * Get current score status.
     * @return score counter value.
     */
    public int getScore() {
        return score;
    }

    public Grenades getGrenades() {
        return this.grenades;
    }

    /**
     * Get number of achieved badges.
     * @return number of badges between 0..3,
     */
    public int getBadges() {
        if (enemyCount == 0)
            return MathUtils.clamp(score / badgeScale, 0, 3);
        return 0;
    }

    /**
     * Update statuses.
     * @return true if game continues, false if game is end.
     */
    public boolean update() {
        if (gameFinalized)
            return false;

        if (enemyCount <= 0)
            return false;

        if (grenades.getCount() == 0)
            return false;

        return true;
    }

    /**
     * Count final score and return level cleared status.
     * @return true if player cleared the level.
     */
    public boolean countFinalScore() {
        if (gameFinalized == false) {
            gameFinalized = true;

            score += grenades.getCount() * 150;
        }

        return enemyCount == 0;
    }

    @Override
    public String toString() {
        return "Score: " + score;
    }
}
