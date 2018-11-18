package Visual;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    private int WIDTH;
    private int HEIGHT;

    public RightPanel(int DEFAULTWIDTH, int DEFAULTHEIGHT) {
        super();
        this.WIDTH = DEFAULTWIDTH;
        this.HEIGHT = DEFAULTHEIGHT;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(WIDTH /2, HEIGHT));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLUE);
    }
}
