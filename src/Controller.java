import AccessPoints.Door;
import Visual.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class Controller {
    private View view;
    private Model model;

    private DateTimeFormatter dtf;

    Controller(){
        model = new Model();
        //model.jsonEntities("files/JSONentities");
        model.jsonEntities("files/entities_JSON.json");

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
    //used for creating any data at all
    private void createPersons() {
        model.createPersonsAtStart();
    }
    /*
    END OF SIMULATION
     */
    //returns a string with all students
    void getAllStudentsAsString() {
        view.addDataToOutputText(model.createAllStudentsString());
    }
    void getEntitiesAsString() {
        view.addDataToOutputText(model.getAllEntities());
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
    void getStudentByIDNAME(String id) {
        if(id.matches("^[0-9]+$")) {
            view.addDataToOutputText(model.searchStudentList(id));
        }else if (id.matches("^[a-zA-Z\\-.']*$")) {
            view.addDataToOutputText(model.searchStudentList(id));
        }else {
            view.addDataToOutputText("Not allowed symbols detected. :(");
        }
    }

    private void contrAddLogToview() {
        view.addLogToOutput(model.getLatestJSONLOG());
    }

    void inputLOG() {
        model.inputNewDateLog();
    }
    void temporaryFunction() {
//
    }
}
