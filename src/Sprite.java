import java.awt.*; 


public abstract class Sprite {
    protected int x;
    protected int y;
    protected int width;
    protected int height;


    public abstract void draw(Graphics2D g);
}
