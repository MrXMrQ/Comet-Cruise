import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainMenu {
    //Frames
    MyFrame myFrame;

    //Labels
    JLabel playerLabel;
    Asteroid asteroid;

    boolean left;
    boolean right;
    boolean asteroidDestroyed = true;

    Thread mainMenuWindowThread;
    Thread collisonThread;
    Thread asteroidMoverThread;
    Thread asteroidDetectedThread;

    int index = 0;

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
        myFrame.add(playerLabel);
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
        collisonThread.start();
        asteroidMoverThread.start();
        myFrame.getContentPane().setBackground(Color.BLACK);
    }

    public void collision() {
        while (collisonThread.isAlive()) {
            if (playerLabel.getX() <= myFrame.getY() || playerLabel.getX() + playerLabel.getWidth() >= myFrame.getWidth()) {
                myFrame.remove(playerLabel);
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
                    SwingUtilities.updateComponentTreeUI(myFrame);
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}