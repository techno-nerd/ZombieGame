import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

enum GameState {
    Welcome,
    Playing,
    Ended
}

public class ZombieSurvivalGame extends JPanel implements KeyListener {
    public static final int UPDATE_FREQ = 16;
    public static final int GAME_SCREEN_WIDTH = 800;
    public static final int GAME_SCREEN_HEIGHT = 600;
    public static final double PISTOL_FIRERATE = 100;
    public static final int ZOMBIE_TOLERANCE = 5;
    public static final int NUM_ZOMBIES = 5;

    private Player player;
    private ArrayList<Zombie> zombies;
    private ArrayList<Bullet> bullets;
    private long timeSinceUpdate;
    private GameState gameState;

    public ZombieSurvivalGame() {
        zombies = new ArrayList<Zombie>();
        bullets = new ArrayList<Bullet>();
        player = new Player();
        timeSinceUpdate = 0;
        setFocusable(true);
        addKeyListener(this);
        gameState = GameState.Welcome;
    }
    
    public void startGame() {
       
        while (true) {
            if(System.currentTimeMillis() - timeSinceUpdate >= UPDATE_FREQ) {
                int result = updateGame();
                if(result == -1) {
                    break;
                }
                else {
                    player.updateScore(result);
                }
                repaint();
                timeSinceUpdate = System.currentTimeMillis();
            }

            if(zombies.size() < NUM_ZOMBIES) {
                zombies.add(new Zombie(GAME_SCREEN_WIDTH, (int) (GAME_SCREEN_HEIGHT*0.2)));
            }
            
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawString(""+player.getScore(), GAME_SCREEN_WIDTH-50, 50);
        drawSprites(g2D);
    }

    private void drawSprites(Graphics2D g2D) {
        player.draw(g2D);

        for(int i=0; i<zombies.size(); i++) {
            zombies.get(i).draw(g2D);
        }

        for(int i=0; i<bullets.size(); i++) {
            bullets.get(i).draw(g2D);
        }
    }

    /**
     * Updates the zombies and bullets
     * @return -1 if zombie is touching player | Else, it returns the score
     */
    private int updateGame() {
        int score = 0;
        
        //Handles zombies and checks if zombie is touching player
        int z = 0;
        while(z < zombies.size()) {
            Zombie zombie = zombies.get(z);

            Rectangle zombieRect = zombie.getRect();
            if(zombieRect.getBounds().intersects(player.getRect())) {
                return -1;
            }

            if(zombie.validatePosition(GAME_SCREEN_HEIGHT)) {
                //zombie.move(player.getX(), player.getY());
                zombie.move();
                z++;
            }
            else {
                zombies.remove(z);
            }
            
        }

        //If the bullet is touching a zombie, 
        int b=0;
        while(b<bullets.size()) {
            Bullet bullet = bullets.get(b);

            if(checkBulletHit(bullet, zombies)) {
                score++;
                bullets.remove(b);
                continue;
            }
            
            //Ensures bullets that are no longer visible are deleted
            if(bullet.validatePosition(GAME_SCREEN_HEIGHT)) {
                bullet.move();
                b++;
            }
            else {
                bullets.remove(b);
            }
        }
        
        return score;

    }

    public boolean checkBulletHit(Bullet bullet, ArrayList<Zombie> zombies) {
        Rectangle bulletRect = bullet.getRect();
        for(int i=0; i<zombies.size(); i++) {
            Zombie zombie = zombies.get(i);
            if(bulletRect.getBounds().intersects(zombie.getRect())) {
                zombies.remove(i);
                return true;
            }
        }
        return false;
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            if(player.getX() > 0) {
                player.move(-1);
            }
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            if(player.getX() < GAME_SCREEN_WIDTH-player.getWidth()) {
                player.move(1);
            }
        } else if (keyCode == KeyEvent.VK_SPACE) {
            Bullet bullet = player.shoot();
            if(bullet != null) {
                bullets.add(bullet);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}
