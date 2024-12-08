import javax.swing.JFrame;

public class ZombieGameRunner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Zombie Survival Game");
        ZombieSurvivalGame game = new ZombieSurvivalGame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(ZombieSurvivalGame.GAME_SCREEN_WIDTH, ZombieSurvivalGame.GAME_SCREEN_HEIGHT);
        frame.add(game);
        frame.setVisible(true);

        game.startGame();
    }
}
