package AccessPoints;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class automatic_door implements Entity {

    private Shape shape;
    private Color color;
    private Stroke stroke;

    private String name;
    private int id;
    private String loc;
    private boolean state;
    private List<Long> access;
    private int key;

    private final int width = 20;
    private final int height = 20;
    private int x1;
    private int y1;

    public automatic_door(String name, int id, String loc,
                          int x1, int y1, Shape shape,
                          Color color, Stroke stroke) {
        this.shape = shape;
        this.color = color;
        this.stroke = stroke;
        this.name = name;
        this.id = id;
        this.loc = loc;
        this.x1 = x1;
        this.y1 = y1;
        this.access = new ArrayList<>();
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void draw(Graphics2D g2) {
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();

        g2.setColor(color);
        g2.setStroke(stroke);
        g2.fill(shape);

        g2.setColor(oldColor);
        g2.setStroke(oldStroke);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getLoc() {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }
}
