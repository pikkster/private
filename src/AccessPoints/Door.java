package AccessPoints;
import Person.Student;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Door extends JPanel implements Entity {

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
    private Color color;

    public Door(String name, int id, String loc, int x1, int y1) {
        this.name = name;
        this.id = id;
        this.loc = loc;
        this.x1 = x1;
        this.y1 = y1;
        this.access = new ArrayList<>();
    }

    public int getX() {
        return x1;
    }
    public int getY () {
        return y1;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState(){
        return this.state;
    }
    void setKey (int key) {
        this.key = key;
    }
    public int getKey () {
        return this.key;
    }
    public void setAccess(Student student) {
        access.add(student.getPrivate_key());
    }

    public boolean getAccess(Student student) {
        return access.contains(student.getPrivate_key());
    }


    public void changeColor(int i) {
        switch (i) {
            case 1:
                color = Color.BLACK;
                break;
            case 2:
                color = Color.RED;
                break;
            case 3:
                color = Color.PINK;
                break;
            case 4:
                color = Color.GREEN;
                break;
        }
    }
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.fillRect(x1, y1, width, height);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getLoc() {
        return this.loc;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
