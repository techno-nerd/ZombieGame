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
    
    public static final int NUM_ZOMBIES = 5;
    /**
     * The chance of a zombie adjusting it's angle every time the game is updated
     */
    public static final double ADJUST_ANGLE_PROBA = 0.01;
    public static final int MAX_SPECIAL_MOVES = 3;


    private Player player;
    private int numSpecialMoves;

    private ArrayList<Zombie> zombies;
    private ArrayList<Bullet> bullets;
    private long timeSinceUpdate;
    private GameState gameState;
    private ArrayList<Integer> keysPressed = new ArrayList<Integer>();


    public ZombieSurvivalGame() {
        zombies = new ArrayList<Zombie>();
        bullets = new ArrayList<Bullet>();

        player = new Player();
        numSpecialMoves = 0;

        timeSinceUpdate = 0;
        setFocusable(true);
        addKeyListener(this);

        setBackground(Color.DARK_GRAY);

        gameState = GameState.Welcome;
    }
    
    
    public void startGame() {
        while (true) {
            if(System.currentTimeMillis() - timeSinceUpdate >= UPDATE_FREQ) {
                switch(gameState) {
                    case Welcome: {
                        repaint();
                        try {
                            Thread.sleep(5000);
                        } 
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        gameState = GameState.Playing;
                        break;
                    }

                    case Playing: {
                        repaint();
                        int result = updateGame();
                        if(result == -1) {
                            gameState = GameState.Ended;
                        }
                        else {
                            player.updateScore(result);
                        }
                        break;
                    }

                    case Ended: {
                        repaint();
                        break;
                    }
                }

                timeSinceUpdate = System.currentTimeMillis();
        
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        switch (gameState) {
            case Welcome: {
                drawWelcome(g2D);
                break;
            }
                
            case Playing: {
                drawScore(g2D);
                drawSprites(g2D);
                break;
            }

            case Ended: {
                drawEnd(g2D);
                break;
            }
        
        }
        
    }

    private void drawWelcome(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.drawString("Welcome! You will be shooting some zombies today", GAME_SCREEN_WIDTH/2 - 250, GAME_SCREEN_HEIGHT/2 - 50);
        g2d.drawString("Space to shoot, Arrow Keys to navigate and E to switch the gun from left to right hand", GAME_SCREEN_WIDTH/2 - 250, GAME_SCREEN_HEIGHT/2);
        g2d.drawString("Q to kill all zombies (you can only use this 3 times!)", GAME_SCREEN_WIDTH/2 - 250, GAME_SCREEN_HEIGHT/2 + 50);
        g2d.drawString("Your gun will upgrade as you score points. Check the README file for details", GAME_SCREEN_WIDTH/2 - 250, GAME_SCREEN_HEIGHT/2 + 100);

    }

    private void drawScore(Graphics2D g2D) {
        g2D.setColor(Color.WHITE);
        g2D.drawString("Score: "+player.getScore(), GAME_SCREEN_WIDTH-150, 50);
        g2D.drawString("Special Moves: "+(MAX_SPECIAL_MOVES-numSpecialMoves), GAME_SCREEN_WIDTH-150, 100);
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

    private void drawEnd(Graphics2D g2d) {
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 64));
        g2d.setColor(Color.RED);
        g2d.drawString("You died!", GAME_SCREEN_WIDTH/2 - 300, GAME_SCREEN_HEIGHT/2 - 50);
        g2d.setColor(Color.WHITE);
        g2d.drawString(String.format("Your score: %d", player.getScore()), GAME_SCREEN_WIDTH/2 - 300, GAME_SCREEN_HEIGHT/2 + 50);
    }

    /**
     * Handles key presses and updates the zombies and bullets
     * @return -1 if zombie is touching player | Else, it returns the score
     */
    private int updateGame() {
        handleMovement();

        int zResult = handleZombies();
        if(zResult == -1) {
            return -1;
        }
        
        return handleBullets();

    }

    private boolean checkBulletHit(Bullet bullet) {
        Rectangle bulletRect = bullet.getRect();
        for(int i=0; i<zombies.size(); i++) {
            Zombie zombie = zombies.get(i);
            if(bulletRect.getBounds().intersects(zombie.getRect(true))) {
                zombies.remove(i);
                return true;
            }
        }
        return false;
    }

    private void handleMovement() {
        for(int i=0; i<keysPressed.size(); i++) {
            int keyCode = keysPressed.get(i);
            if (keyCode == KeyEvent.VK_LEFT) {
                if(player.getX() > 0) {
                    player.move(-1);
                }
            } 
            
            if (keyCode == KeyEvent.VK_RIGHT) {
                if(player.getX() < GAME_SCREEN_WIDTH-player.getWidth()) {
                    player.move(1);
                }
            } 
        }
    }

    /**
     * Adds, moves, removes and checks for contact with player.  
     * @return -1 if touching player | 0 otherwise
     */
    private int handleZombies() {
        //Adds zombies
        if(zombies.size() < NUM_ZOMBIES) {
            zombies.add(new Zombie(GAME_SCREEN_WIDTH, (int) (GAME_SCREEN_HEIGHT*0.2), player.getX(), player.getY()));
        }

        //Handles zombies and checks if zombie is touching player
        int z = 0;
        while(z < zombies.size()) {
            Zombie zombie = zombies.get(z);

            //Checks if zombie is touching player
            Rectangle zombieRect = zombie.getRect(false);
            if(zombieRect.getBounds().intersects(player.getRect())) {
                return -1;
            }

            //Moves zombies with valid positions
            if(zombie.validatePosition(GAME_SCREEN_HEIGHT)) {
                if(Math.random() < ADJUST_ANGLE_PROBA) {
                    zombie.updateAngle(player.getX(), player.getY());
                }
                zombie.move();
                z++;
            }
            //Deletes zombies that are off the screen
            else {
                zombies.remove(z);
            }
            
        }
        return 0;
    }

    /**
     * Moves, removes and checks if bullet is touching zombie
     * @return score
     */
    private int handleBullets() {
        int score = 0;
        //If the bullet is touching a zombie, 
        int b = 0;
        while(b < bullets.size()) {
            Bullet bullet = bullets.get(b);

            if(checkBulletHit(bullet)) {
                score++;
                bullets.remove(b);
                continue;
            }
            
            //Ensures bullets that are no longer visible are deleted
            if(bullet.validatePosition()) {
                bullet.move();
                b++;
            }
            else {
                bullets.remove(b);
            }
        }

        return score;
    }

    /**
     * Uses the Special Move of eradicating all zombies
     */
    private void useSpecialMove() {
        if(numSpecialMoves >= MAX_SPECIAL_MOVES) {
            return;
        }
        else {
            zombies.clear();
            numSpecialMoves++;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if((keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT)) {
            keysPressed.add(keyCode);
        }

        if(keyCode == KeyEvent.VK_E) {
            player.switchHands();
        }

        if(keyCode == KeyEvent.VK_SPACE) {
            Bullet bullet = player.shoot();
            if(bullet != null) {
                bullets.add(bullet);
            }
        }

        if(keyCode == KeyEvent.VK_Q) {
            useSpecialMove();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
        int i = 0;
        while(i < keysPressed.size()) {
            if(keysPressed.get(i) == keycode) {
                keysPressed.remove(i);
            }
            else {
                i++;
            }   
        }
    }

}
