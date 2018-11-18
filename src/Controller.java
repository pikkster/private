import AccessPoints.Door;
import Person.Student;
import Visual.*;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

class Controller {
    //enables usage without model or view gets broken
    private View view;
    private Model model;
    private int logToFetch = 0;

    Controller(){

        view = new View(this);
        model = new Model();
        createPersons();
        model.importEntities("files/locationsForEnt");

        startTimerForSimulation();
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
        String logString;

        if(model.getEntList().get(random_door).getAccess(model.getAllStudents().get(rand))) {
            //System.out.println("Got access to door: " + model.getEntList().get(random_door).getName());
            //System.out.println("Person who accessed door: " + model.getAllStudents().get(rand).getName());
            //System.out.println("Private key: " + model.getAllStudents().get(rand).getPrivate_key());
            //model.getMainJsonObject();

            logString = "Got access to door: " + model.getEntList().get(random_door).getName() +
                    "\nPerson who accessed door: " + model.getAllStudents().get(rand).getName() +
                    "\nPrivate key: " + model.getAllStudents().get(rand).getPrivate_key()+"\n";

            long millis = System.currentTimeMillis();
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            System.out.println(days);
            model.inputNewDateLog(Long.toString(days));

            model.inputLog(Long.toString(days),
                    model.getAllStudents().get(rand).getName(),
                    Long.toString(TimeUnit.MILLISECONDS.toMinutes(millis)));

            model.getEntList().get(random_door).setState(true);
            changeColor(model.getEntList().get(random_door), rand);
        } else {
            //System.out.println("Denied access to door: " + model.getEntList().get(random_door).getName());
            //System.out.println("Person who accessed door: " + model.getAllStudents().get(rand).getName());
            //System.out.println("Private key: " + model.getAllStudents().get(rand).getPrivate_key());
            logString = "Denied access to door: " + model.getEntList().get(random_door).getName() +
                    "\nPerson who accessed door: " + model.getAllStudents().get(rand).getName() +
                    "\nPrivate key: " + model.getAllStudents().get(rand).getPrivate_key()+"\n";
            //model.getMainJsonObject();

            long millis = System.currentTimeMillis();
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            model.inputNewDateLog(Long.toString(days));

            model.inputLog(Long.toString(days),
                    model.getAllStudents().get(rand).getName(),
                    Long.toString(TimeUnit.MILLISECONDS.toHours(millis)));

            model.getEntList().get(random_door).setState(false);

            changeColor(model.getEntList().get(random_door), rand);
        }
        model.addLog(logString);
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

    void contrAddLogToview() {
        view.addLogToOutput(model.getLatestLog());
        //String log = model.getLogsList().get(logToFetch++);
        //view.addLogToOutput(log);
    }



    //not useful in final
    void getStudentsKeys() {
        Map<Long, Student> test = model.generateKeys();
        for(Map.Entry m : test.entrySet()) {
            System.out.println(m.getKey() + " " + m.getValue());
        }
    }
}
