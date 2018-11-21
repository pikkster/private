package Person;
import AccessPoints.*;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private int ID;
    private long private_key;
    private List<Long> acc;
    private List<Door> history;

    public Student(String name, int ID){
        this.name = name;
        this.ID = ID;
        acc = new ArrayList<>();
        setPrivate_key();

    }
    public String getName() {
        return this.name;
    }
    public int getID () {
        return this.ID;
    }
    private void setPrivate_key () {
        this.private_key = (long) (Math.random()*Short.MAX_VALUE);
    }
    public long getPrivate_key () {
        return this.private_key;
    }
    public String toString () {
        return this.name + " " + this.ID;
    }
}
