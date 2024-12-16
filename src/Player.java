import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


enum WeaponType {
    Pistol,
    Shotgun,
    AK47,
    MachineGun
}


public class Player extends Sprite {
    /**
     * How much is subtracted from speed when the gun is upgraded
     */
    public static final double SPEED_REDUCTION = 0.3;


    private Gun gun;
    private WeaponType weaponType;
    private int score;
    private double speed;
    private BufferedImage image;

    
    public Player() {
        score = 0;
        speed = 1.5;
        this.x = 350;
        this.y = 450;
        this.width = 50;
        this.height = 50;
        this.weaponType = WeaponType.Pistol;
        this.gun = new Gun(Gun.PISTOL_FIRERATE, x, y);

        try {
            image = ImageIO.read(new File("media/Player.png"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, (int)x, (int)y, width, height, null);
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

    /**
     * Adds <code>points</code> to the player's <code>score</code>
     * If the threshold for next gun is crossed, then player gets the new gun
     * @param points Number of points to be added
     */
    public void updateScore(int points) {
        score += points;
        
        if(score < 20 && score >= 10 && weaponType != WeaponType.Shotgun) {
            gun = new Gun(Gun.SHOTGUN_FIRERATE, x, y, gun.getHand(), width);
            weaponType = WeaponType.Shotgun;
            speed -= SPEED_REDUCTION;
        }
        else if(score < 50 && score >= 20 && weaponType != WeaponType.AK47) {
            gun = new Gun(Gun.AK47_FIRERATE, x, y, gun.getHand(), width);
            weaponType = WeaponType.AK47;
            speed -= SPEED_REDUCTION;
        }
        else if(score >= 50 && weaponType != WeaponType.MachineGun) {
            gun = new Gun(Gun.MACHINE_GUN_FIRERATE, x, y, gun.getHand(), width);
            weaponType = WeaponType.MachineGun;
            speed -= SPEED_REDUCTION;
        }
    }

    public void switchHands() {
        gun.switchHand(width);
    }

    //Getters
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getSpeed() {
        return this.speed;
    }

    public int getScore() {
        return this.score;
    }

    public int getWidth() {
        return this.width;
    }

    public Rectangle getRect() {
        return new Rectangle((int)x, (int)y, width, height);
    }


}
