package Person;
import AccessPoints.*;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private String firstName;
    private String surname;
    private int ID;
    private long private_key;
    private List<Long> acc;
    private List<Door> history;

    public Student(String name ,int ID){
        this.firstName = name;
        this.ID = ID;
        acc = new ArrayList<>();
        setPrivate_key();

    }
    public String getFirstName() {
        return this.firstName;
    }
    void setSurname () {
        if (surname == null) {
            int randomSurname = (int)((Math.random() * 10) + 1) ;
            switch (randomSurname) {
                case 1:
                    this.surname = "Smith";
                case 2:
                    this.surname = "Johnson";
                case 3:
                    this.surname = "Kent";
                case 4:
                    this.surname = "Gardner";
                case 5:
                    this.surname = "McKinley";
                case 6:
                    this.surname = "Gibson";
                case 7:
                    this.surname = "Meyer";
                case 8:
                    this.surname = "Avery";
                case 9:
                    this.surname = "Guzman";
                case 10:
                    this.surname = "Kennedy";
            }
        }
    }
    public String getSurname () {
        return this.surname;
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
        return this.firstName + " " + this.ID;
    }
}
