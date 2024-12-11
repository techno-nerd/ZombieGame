import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Player extends Sprite {
    private Gun gun;
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
        this.gun = new Gun(ZombieSurvivalGame.PISTOL_FIRERATE, x, y);

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

    public void updateScore(int points) { //Is there a better way to do this? --> Use enum or boolean
        score += points;
        if(score < 20 && score >= 10) {
            gun = new Gun(ZombieSurvivalGame.SHOTGUN_FIRERATE, x, y, gun.getHand(), width);
        }
        else if(score < 50 && score >= 20) {
            gun = new Gun(ZombieSurvivalGame.AK47_FIRERATE, x, y, gun.getHand(), width);
        }
        else if(score >= 50) {
            gun = new Gun(ZombieSurvivalGame.MACHINE_GUN_FIRERATE, x, y, gun.getHand(), width);
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
