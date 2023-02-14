import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    public MyFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Flight to the moon");
        setSize(600, 800);
        setResizable(false);
        setVisible(true);
        setLayout(null);

    }

    /*public void paint(Graphics g) {
        for (int i = 0; i < 1000; i++) {
            g.setColor(Color.WHITE);
            g.fillRect((int)(Math.random()*600),(int)(Math.random()*800),1,1);
        }
    }*/
}