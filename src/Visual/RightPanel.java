package Visual;
import AccessPoints.automatic_door;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RightPanel extends JPanel {

    private int WIDTH;
    private int HEIGHT;
    private List<automatic_door> entityToPaint = new ArrayList<>();

    public RightPanel(int DEFAULTWIDTH, int DEFAULTHEIGHT) {
        super();
        this.WIDTH = DEFAULTWIDTH;
        this.HEIGHT = DEFAULTHEIGHT;

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(WIDTH /2, HEIGHT));
    }

    public void addMyDrawable(automatic_door myDoor) {
        entityToPaint.add(myDoor);
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        for (automatic_door AD : entityToPaint) {
            AD.draw(g2);
        }


/*
        for(Door d : entityToPaint) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLUE);
            g2.fillRect(d.getX_pos(),d.getY_pos(),20,20);
        }*/


    }
}
