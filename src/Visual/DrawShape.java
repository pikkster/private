package Visual;
import javax.swing.*;
import java.awt.*;

public class DrawShape extends JPanel {

    private final int width = 40;
    private final int height = 40;
    private int x1;
    private int y1;

    public DrawShape(int x, int y) {
        this.x1 = x;
        this.y1 = y;
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x1, y1, width, height);
    }


}
