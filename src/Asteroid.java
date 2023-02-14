import javax.swing.*;
import java.awt.*;

public class Asteroid extends JLabel {
    public Asteroid() {
        setBounds((int) (Math.random() * 600), 0, 60, 60);
        setBackground(Color.GRAY);
        setOpaque(true);
    }
}
