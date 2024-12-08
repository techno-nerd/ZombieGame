import java.awt.*;


public class Zombie extends Sprite {
    private int speed;

    public Zombie(int width, int height) {
        this.x = (int)(Math.random()*width);
        this.y = height;
        this.width = 50;
        this.height = 50;
        speed = 1;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.fillOval(x, y, width, height);
    }

    /**
     * @param x x coordinate towards which it moves
     * @param y y coordinate towards which it moves
     * 
     * Moves `speed` distance towards given coordinates
     */
    public void move(int x, int y) {
        try {
            double angle = Math.atan((double)((x-this.x)/(y-this.y)));
            this.x += (Math.sin(angle)*speed);
            this.y += (Math.cos(angle)*speed);
        }

        catch(Exception e) {
            System.out.println("Touching player");
        }
        
    }

    public void move() {
        y += speed;
    }

    public boolean validatePosition(int screenHeight) {
        return (this.y < screenHeight);
    }
    /**
     * 
     * @return Rectangle object with slightly smaller width and height (defined in <code>Constants.ZOMBIE_TOLERANCE</code>)
     */
    public Rectangle getRect() {
        return new Rectangle(x, y, width-ZombieSurvivalGame.ZOMBIE_TOLERANCE, height-ZombieSurvivalGame.ZOMBIE_TOLERANCE);
    }
    
}
