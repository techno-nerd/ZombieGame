import java.awt.*;


public class Player extends Sprite {
    private Gun gun;
    private int score;
    private int speed;

    public Player() {
        score = 0;
        speed = 5;
        this.x = 350;
        this.y = 450;
        this.width = 50;
        this.height = 50;
        this.gun = new Gun(ZombieSurvivalGame.PISTOL_FIRERATE, x, y);
    }


    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
        gun.draw(g);
    }

    /**
     * Moves the player and the gun
     * @param direction -1 for left, and 1 for right
     */
    public void move(int direction) {
        this.x += direction*speed;
        gun.updateGun(direction, speed);
    }

    /**
     * Calls the `gun.shoot()` method
     * @return Bullet if time has passed | null otherwise
     */
    public Bullet shoot() {
        return gun.shoot();
    }

    public void updateScore(int points) {
        score += points;
    }


    //Getters
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getScore() {
        return this.score;
    }

    public int getWidth() {
        return this.width;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }


}
