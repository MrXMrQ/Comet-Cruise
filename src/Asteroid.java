import javax.swing.*;
import java.awt.*;

public class Asteroid extends JLabel {

    public Asteroid() {
        int size = (int) (Math.random() * (60 - 40)) + 40;
        setBounds((int) (Math.random() * 600), 0, size, size);
        setBackground(Color.GRAY);
        setOpaque(true);
    }
}
