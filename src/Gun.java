import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Defines the gun with which the player shoots
 */
enum Hand {
    Left,
    Right
}


public class Gun extends Sprite {
    public static final double PISTOL_FIRERATE = 400;
    public static final double SHOTGUN_FIRERATE = 300;
    public static final double AK47_FIRERATE = 150;
    public static final double MACHINE_GUN_FIRERATE = 50;


    /**
     * Time in milliseconds between two shots
     */
    private double fireRate;
    private long timeSinceShot;
    
    /**
     * Which hand the player has the gun in
     * Defaults to <code>Left</code> at the start of the game
     */
    private Hand hand;
    private BufferedImage image;

    public Gun(double fireRate, double x, double y) {
        this.fireRate = fireRate;
        timeSinceShot = 0;
        this.x = x;
        this.y = y;
        width = 20;
        height = 50;
        hand = Hand.Left;

        try {
            if(fireRate == PISTOL_FIRERATE) {
                image = ImageIO.read(new File("media/Pistol.png"));
            }

            if(fireRate == SHOTGUN_FIRERATE) {
                image = ImageIO.read(new File("media/Shotgun.png"));
            }

            if(fireRate == AK47_FIRERATE) {
                image = ImageIO.read(new File("media/AK47.png"));
            }

            if(fireRate == MACHINE_GUN_FIRERATE) {
                image = ImageIO.read(new File("media/MachineGun.png"));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    public Gun(double fireRate, double x, double y, Hand hand, int playerWidth) {
        this(fireRate, x, y);
        if(hand == Hand.Right) {
            this.switchHand(playerWidth);
        }
    }

    
    /**
     * 
     * @return Bullet if <code>fireRate</code> time has passed | null otherwise
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
        g.drawImage(image, (int)x, (int)y, width, height, null);
    }

    /**
     * This moves the gun in the same direction as the player
     * @param direction -1 for left, 0 for no change and 1 for right (this is only for the position of the gun)
     * @param playerSpeed Pixels the player moves per step
     */
    public void updateGun(int direction, double playerSpeed) {
        this.x += direction*playerSpeed;
    }

    /**
     * Toggles the hand in which the gun is by updating the x coordinate
     * @param playerWidth Width (in pixels) of the player sprite
     */
    public void switchHand(int playerWidth) {
        if(hand == Hand.Left) {
            x = x + playerWidth - width;
            hand = Hand.Right;
        }
        else {
            x = x - playerWidth + width;
            hand = Hand.Left;
        }
    }

    public Hand getHand() {
        return hand;
    }

}
