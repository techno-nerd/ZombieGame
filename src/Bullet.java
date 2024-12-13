import java.awt.*;


public class Bullet extends Sprite {
    private double speed;
    
    public Bullet(double x, double y) {
        this.x = x;
        this.y = y;
        speed = 5;
        width = 5;
        height = 5;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillOval((int)x, (int)y, width, height);
    }

    public void move() {
        this.y -= speed;
    }

    /**
     * Assumes that top left corner = (0, 0)
     * @return true if the bullet is within the frame; false otherwise
     */
    public boolean validatePosition() {
        return(y > -height);
    }

    public Rectangle getRect() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
