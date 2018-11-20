import AccessPoints.Door;
import AccessPoints.automatic_door;
import Person.Student;
import Visual.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class Controller {
    private View view;
    private Model model;

    private DateTimeFormatter dtf;

    Controller(){
        model = new Model();
        model.importEntities("files/locationsForEnt");

        view = new View(this);
        createPersons();

        dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        /*
            SIMULATION
         */
        startTimerForSimulation();
        /*

         */
    }

    private void startTimerForSimulation() {

        /*
        ONLY FOR SIMULATION not efficient
         */
        for(Door d : model.getEntList()) {
            for(int i = 0; i < 100;i++) {
                d.setAccess(model.getAllStudents().get(i));
            }
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //System.out.println("Doing task");
                simulateMovement();
            }
        }, 2 *1000, 5 *1000);
    }

    private void simulateMovement() {
        int rand = (int)(Math.random()*200);
        int random_door = (int) (Math.random()*model.getEntList().size());

        if(model.getEntList().get(random_door).getAccess(model.getAllStudents().get(rand))) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDate date = currentTime.toLocalDate();

            model.inputLog(String.valueOf(date),
                    model.getAllStudents().get(rand),
                    LocalTime.now().format(dtf),
                    model.getEntList().get(random_door),
                    model.getEntList().get(random_door).getAccess(model.getAllStudents().get(rand)));

            model.getEntList().get(random_door).setState(true);
            changeColor(model.getEntList().get(random_door), rand);

        } else {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDate date = currentTime.toLocalDate();

            model.inputLog(String.valueOf(date),
                    model.getAllStudents().get(rand),
                    LocalTime.now().format(dtf),
                    model.getEntList().get(random_door),
                    model.getEntList().get(random_door).getAccess(model.getAllStudents().get(rand)));

            model.getEntList().get(random_door).setState(false);

            changeColor(model.getEntList().get(random_door), rand);
        }
        contrAddLogToview();
    }
    /*
    END OF SIMULATION
     */

    //used for creating any data at all
    private void createPersons() {
        model.createNewPerson();
    }
    //returns a string with all students
    String getAllStudents() {
        StringBuilder sb = new StringBuilder();
        for (Student s : model.getAllStudents()) {
            sb.append("Name: ")
                    .append(s.getName())
                    .append("\nID: ")
                    .append(s.getID())
                    .append("\n-------------------\n");
        }
        return String.valueOf(sb);
    }
    String getEntitiesAsString() {
        return model.getAllEntities();
    }
    List<Door> getEntityList() {
        return model.getEntList();
    }
    Door createShape(String name, String id, String location, String x_pos, String y_pos) {
        return model.createNewEntity(name,
                Integer.parseInt(id),
                location,
                Integer.parseInt(x_pos),
                Integer.parseInt(y_pos));
    }
    void changeColor (Door door, int rand) {
        try {
            if (door.getAccess(model.getAllStudents().get(rand))) {
                door.changeColor(4);
                view.VIEW_UPDATER();
            } else {
                door.changeColor(2);
                view.VIEW_UPDATER();
            }
            Thread.sleep(1000);
            door.changeColor(1);
            view.VIEW_UPDATER();
        } catch (Exception e) {
            //
        }
    }
    PaintImage drawImage() {
        return new PaintImage();
    }
    //Search from UI to get a Person
    String getStudentByIDNAME(String id) {
        if(id.matches("^[0-9]+$")) {
            return model.searchStudentList(id);
        }else if (id.matches("^[a-zA-Z\\-.']*$")) {
            return model.searchStudentList(id);
        }else {
            return "Not allowed symbols detected. :(";
        }
    }

    private void contrAddLogToview() {
        view.addLogToOutput(model.getLatestJSONLOG());
    }
}
