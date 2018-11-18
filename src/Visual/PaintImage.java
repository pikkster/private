package Visual;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PaintImage extends JPanel {
    public static BufferedImage image;
    private int prefW;
    private int prefH;

    public PaintImage () {
        super();
        try{
            BufferedImage img = ImageIO.read(new File("files/floor.png"));
            prefH = img.getHeight();
            prefW = img.getWidth();

            image = new BufferedImage(prefW,prefH,BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            g.drawImage(img,0,0,null);
            g.dispose();

        } catch (Exception e) {

        }
    }
    public void paintComponent(Graphics g) {
        g.drawImage(image,10,10,null);
    }
}
