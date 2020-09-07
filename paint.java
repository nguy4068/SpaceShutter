import javax.swing.*;
import java.awt.*;

public class paint extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game.game.repaint(g);
    }
}
