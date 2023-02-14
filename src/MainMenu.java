import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainMenu {
    //Frames
    MyFrame myFrame;

    //Labels
    JLabel playerLabel;
    Asteroid asteroid = new Asteroid();

    //Booleans
    boolean left;
    boolean right;
    boolean asteroidDestroyed = true;

    //Threads
    Thread mainMenuWindowThread;
    Thread collisonThread;
    Thread asteroidMoverThread;

    int difficulty = 100;

    public MainMenu() {
        mainMenuWindowThread = new Thread(this::mainMenuWindow);
        mainMenuWindowThread.start();

        collisonThread = new Thread(this::collision);

        asteroidMoverThread = new Thread(this::asteroidMover);

    }

    public void mainMenuWindow() {
        myFrame = new MyFrame();

        playerLabel = new JLabel("PLAYER", SwingUtilities.CENTER);
        playerLabel.setBounds(150, 600, 100, 100);
        playerLabel.setBackground(Color.GRAY);
        playerLabel.setOpaque(true);

        asteroidMoverThread.start();
        collisonThread.start();

        myFrame.add(playerLabel);
        myFrame.getContentPane().setBackground(Color.BLACK);
        myFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'a' -> {
                        playerLabel.setLocation(playerLabel.getX() - 10, playerLabel.getY());
                        right = false;
                        left = true;
                    }
                    case 'd' -> {
                        playerLabel.setLocation(playerLabel.getX() + 10, playerLabel.getY());
                        right = false;
                        left = true;
                    }
                }
            }
        });
    }

    public void collision() {
        while (collisonThread.isAlive()) {
            if (playerLabel.getX() <= myFrame.getY() || playerLabel.getX() + playerLabel.getWidth() >= myFrame.getWidth()) {
                System.exit(1);
            } else if(playerLabel.bounds().intersects(asteroid.getBounds())) {
                System.exit(1);
            }
        }
    }

    public void asteroidGen() {
        if (asteroidDestroyed) {
            asteroidDestroyed = false;
            asteroid = new Asteroid();
            myFrame.add(asteroid);
            SwingUtilities.updateComponentTreeUI(myFrame);
        }
    }

    public void asteroidMover() {
        while (asteroidMoverThread.isAlive()) {
            try {
                asteroidGen();
                asteroid.setLocation(asteroid.getX(), asteroid.getY() + 10);
                SwingUtilities.updateComponentTreeUI(myFrame);

                if (asteroid.getY() > myFrame.getHeight()) {
                    myFrame.remove(asteroid);
                    asteroidDestroyed = true;
                    if(difficulty > 0) {
                        difficulty--;
                        System.out.println(difficulty);
                    }
                    SwingUtilities.updateComponentTreeUI(myFrame);
                }
                Thread.sleep(difficulty);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}