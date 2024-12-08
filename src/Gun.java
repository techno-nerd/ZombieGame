import java.awt.Color;
import java.awt.Graphics2D;


public class Gun extends Sprite {
    /**
     * Time in milliseconds between two shots
     */
    private double fireRate;
    private long timeSinceShot;
    

    public Gun(double fireRate, int x, int y) {
        this.fireRate = fireRate;
        timeSinceShot = 0;
        this.x = x;
        this.y = y;
        width = 10;
        height = 20;
    }

    /**
     * 
     * @return Bullet if time has passed | null otherwise
     */
    public Bullet shoot() {
        if(System.currentTimeMillis() - timeSinceShot >= fireRate) {
            timeSinceShot = System.currentTimeMillis();
            return new Bullet(this.x, this.y);
        }
        else {
            return null;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
    }

    /**
     * This moves the gun in the same direction as the player
     * @param direction -1 for left, 0 for no change and 1 for right (this is only for the position of the gun)
     * @param playerSpeed Pixels the player moves per step
     */
    public void updateGun(int direction, int playerSpeed) {
        this.x += direction*playerSpeed;
    }
}
