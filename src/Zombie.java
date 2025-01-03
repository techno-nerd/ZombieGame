import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Zombie extends Sprite {
    /**
     * Pixels subtracted from height and width of <code>zombie.getRect(false)</code>
     */
    public static final int ZOMBIE_TOLERANCE = 5;


    private double speed;

    /**
     * Direction in which the zombie is moving
     */
    private double angle;
    private BufferedImage image;


    public Zombie(int width, int height) {
        this.x = (int)(Math.random()*width);
        this.y = height;
        this.width = 25;
        this.height = 50;
        speed = 0.75;
        
        try {
            image = ImageIO.read(new File("media/Zombie.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Zombie(int width, int height, double playerX, double playerY) {
        this(width, height);
        calcAngle(playerX, playerY);
    }

    
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, (int)x, (int)y, width, height, null);
    }


    public void move() {
        if(Double.isNaN(angle)) {
            y += speed;
        }
        else if (angle < 0) {
            this.x -= (Math.cos(angle)*speed); //Since cos(-ve) is positive, and the zombie actually needs to move left
            this.y -= (Math.sin(angle)*speed); //Since sin(-ve) is negative, and the zombie actually needs to move downward
        }
        else {
            this.x += (Math.cos(angle)*speed);
            this.y += (Math.sin(angle)*speed);
        }
    }

    public boolean validatePosition(int screenHeight) {
        return (this.y < screenHeight);
    }
    
    /**
     * @param real Whether the <code>ZOMBIE_TOLERANCE</code> should be subtracted or not. <code>true</code> means not subtracted, <code>false</code> means subtracted 
     */
    public Rectangle getRect(boolean real) {
        if(real) {
            return new Rectangle((int)x, (int)y, width, height);
        }
        else {
            return new Rectangle((int)x, (int)y, width-ZOMBIE_TOLERANCE, height-ZOMBIE_TOLERANCE);
        }
    }

    /**
     * Calculates angle to player
     * @param playerX 
     * @param playerY
     */
    private void calcAngle(double playerX, double playerY) {
        angle = Math.atan2((playerY-y), (playerX-x));
    }

    
    public void updateAngle(double playerX, double playerY) {
        //Does not update if zombie is below y=400
        if(y > 400) {
            return;
        }
        calcAngle(playerX, playerY);
    }
    
}
