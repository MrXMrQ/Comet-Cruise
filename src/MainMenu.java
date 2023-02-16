import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Scanner;

public class MainMenu {
    //Frames
    MyFrame myFrame;

    //Labels
    JLabel playerLabel;
    Asteroid asteroid = new Asteroid();
    Asteroid asteroid1 = new Asteroid();
    Asteroid asteroid2 = new Asteroid();
    Asteroid asteroid3 = new Asteroid();

    JLabel timeLabel;

    //Booleans
    boolean left;
    boolean right;
    boolean asteroidDestroyed = true;
    boolean asteroid1Destroyed = true;
    boolean asteroid2Destroyed = true;
    boolean asteroid3Destroyed = true;

    //Threads
    Thread mainMenuWindowThread;
    Thread collisonThread;
    Thread asteroidMoverThread;
    Thread timerThread;

    int difficulty = 70;
    int timer = 120;
    int score = 0;
    int counterTimesClicked = 0;
    int movementSpeed = 20;

    File newFile = new File("score.txt");

    public MainMenu() {
        mainMenuWindowThread = new Thread(this::mainMenuWindow);
        mainMenuWindowThread.start();

        collisonThread = new Thread(this::collision);

        asteroidMoverThread = new Thread(this::asteroidMover);

        timerThread = new Thread(this::countDown);
    }

    public void mainMenuWindow() {
        myFrame = new MyFrame();
        scoreReader(newFile);
        playerLabel = new JLabel("PLAYER", SwingUtilities.CENTER);
        playerLabel.setBounds(150, 600, 70, 70);
        playerLabel.setBackground(Color.GRAY);
        playerLabel.setOpaque(true);

        asteroidMoverThread.start();
        collisonThread.start();

        myFrame.add(playerLabel);
        myFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'a' -> {
                        playerLabel.setLocation(playerLabel.getX() - movementSpeed, playerLabel.getY());
                        right = false;
                        left = true;
                    }
                    case 'd' -> {
                        playerLabel.setLocation(playerLabel.getX() + movementSpeed, playerLabel.getY());
                        right = false;
                        left = true;
                    }
                }
                if (e.getKeyChar() == 'w' && asteroid.getBackground() == Color.YELLOW && counterTimesClicked < 3 && !collisonThread.isInterrupted()) {
                    score += 100;
                    counterTimesClicked++;
                    SwingUtilities.updateComponentTreeUI(myFrame);
                }
            }
        });

        timeLabel = new JLabel(timer + " Seconds | score: " + score, SwingUtilities.CENTER);
        timeLabel.setBounds(200, 0, 200, 30);
        timeLabel.setBackground(Color.RED);
        timeLabel.setOpaque(true);
        myFrame.add(timeLabel);
        timerThread.start();
        myFrame.getContentPane().setBackground(Color.BLACK);
        SwingUtilities.updateComponentTreeUI(myFrame);
    }

    public void countDown() {
        for (int i = timer; i >= 0; i--) {
            try {
                timeLabel.setText(i + " Seconds | score: " + score);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        timeLabel.setText("VICTORY");
        collisonThread.interrupt();

        String text = Integer.toString(score);
        scoreWriter(newFile, text);
    }

    public void collision() {
        while (!collisonThread.isInterrupted()) {
            if (playerLabel.getX() <= myFrame.getY() || playerLabel.getX() + playerLabel.getWidth() >= myFrame.getWidth()) {
                System.exit(1);
            } else if (playerLabel.bounds().intersects(asteroid.getBounds()) || playerLabel.bounds().intersects(asteroid1.getBounds()) || playerLabel.bounds().intersects(asteroid2.getBounds()) || playerLabel.bounds().intersects(asteroid3.getBounds())) {
                System.exit(1);
            }
        }
    }

    public void asteroidGen() {
        if (asteroidDestroyed) {
            asteroidDestroyed = false;
            if ((int) (Math.random() * 100) > 0) {
                asteroid = new Asteroid();
                asteroid.setBackground(Color.YELLOW);
            } else {
                asteroid = new Asteroid();
            }
            myFrame.add(asteroid);
        } else if (asteroid1Destroyed) {
            asteroid1Destroyed = false;
            asteroid1 = new Asteroid();
            myFrame.add(asteroid1);

        } else if (asteroid2Destroyed) {
            asteroid2Destroyed = false;
            asteroid2 = new Asteroid();
            myFrame.add(asteroid2);

        } else if (asteroid3Destroyed) {
            asteroid3Destroyed = false;
            asteroid3 = new Asteroid();
            myFrame.add(asteroid3);
        }
        SwingUtilities.updateComponentTreeUI(myFrame);
    }

    public void asteroidMover() {
        while (asteroidMoverThread.isAlive()) {
            try {
                asteroidGen();
                asteroid.setLocation(asteroid.getX(), asteroid.getY() + 8);
                asteroid1.setLocation(asteroid1.getX(), asteroid1.getY() + 5);
                asteroid2.setLocation(asteroid2.getX(), asteroid2.getY() + 16);
                asteroid3.setLocation(asteroid3.getX(), asteroid3.getY() + 12);

                if (asteroid.getY() > myFrame.getHeight()) {
                    myFrame.remove(asteroid);
                    asteroidDestroyed = true;
                    counterTimesClicked = 0;
                    destroyedAsteroid();

                } else if (asteroid1.getY() > myFrame.getHeight()) {
                    myFrame.remove(asteroid1);
                    asteroid1Destroyed = true;
                    destroyedAsteroid();

                } else if (asteroid2.getY() > myFrame.getHeight()) {
                    myFrame.remove(asteroid2);
                    asteroid2Destroyed = true;
                    destroyedAsteroid();

                } else if (asteroid3.getY() > myFrame.getHeight()) {
                    myFrame.remove(asteroid3);
                    asteroid3Destroyed = true;
                    destroyedAsteroid();
                }
                Thread.sleep(difficulty);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void scoreWriter(File fileToRead, String text) {
        try {
            FileWriter fileWriter = new FileWriter(fileToRead);
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void scoreReader(File fileToRead) {
        Scanner sc;
        try {
            sc = new Scanner(fileToRead);
            myFrame.setTitle("Last score: " + sc.nextLine());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroyedAsteroid() {
        if (!timeLabel.getText().equals("VICTORY")) {
            score += 10;
        }
        if (difficulty > 10) {
            difficulty--;
        }
        if (movementSpeed < 40) {
            movementSpeed++;
            System.out.println(movementSpeed);
        }
        SwingUtilities.updateComponentTreeUI(myFrame);
    }
}