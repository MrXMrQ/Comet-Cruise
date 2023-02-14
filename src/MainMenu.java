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
    Asteroid[] asteroidsArray = new Asteroid[10];

    boolean left;
    boolean right;

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

        asteroidDetectedThread = new Thread(this::asteroidDetected);
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
        asteroidsArray = asteroidGen();

        collisonThread.start();
        asteroidMoverThread.start();
        asteroidDetectedThread.start();

        myFrame.getContentPane().setBackground(Color.BLACK);
    }

    public void collision() {
        while (collisonThread.isAlive()) {
            if (playerLabel.getX() <= myFrame.getY() || playerLabel.getX() + playerLabel.getWidth() >= myFrame.getWidth() || asteroidDetected()) {
                myFrame.remove(playerLabel);
            } else if (asteroidsArray[index].getY() <= myFrame.getHeight()) {
                asteroidsArray[index].setLocation(asteroidsArray[index].getX(), 0);
            }
        }
    }

    public Asteroid[] asteroidGen() {
        for (int i = 0; i < asteroidsArray.length; i++) {
            asteroidsArray[i] = new Asteroid();
            myFrame.add(asteroidsArray[i]);
            SwingUtilities.updateComponentTreeUI(myFrame);
        }
        return asteroidsArray;
    }

    public void asteroidMover() {
        while (asteroidMoverThread.isAlive()) {
            if (index == asteroidsArray.length) {
                index = 0;
            }
            asteroidsArray[index].setLocation(asteroidsArray[index].getX(), asteroidsArray[index].getY() + 10);
            index++;
            SwingUtilities.updateComponentTreeUI(myFrame);


        }
    }

    public boolean asteroidDetected() {
        while (asteroidDetectedThread.isAlive()) {
            for (int i = 0; i < asteroidsArray.length; i++) {
                if (asteroidsArray[i].equals(playerLabel.getLocation())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
