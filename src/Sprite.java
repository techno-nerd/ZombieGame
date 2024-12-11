import java.awt.*; 


public abstract class Sprite {
    protected double x;
    protected double y;
    protected int width;
    protected int height;


    public abstract void draw(Graphics2D g);
}
