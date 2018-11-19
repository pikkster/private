import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AccessPoints.*;
import Person.*;
import com.oracle.tools.packager.IOUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

class Model {

    //list of all entities
    private List<Door> entities;
    //list of all persons
    private List<Student> listOfPersons;
    //list of all keys for future usage
    private Map<Long, Student> listOfKeys;
    //list of all logs
    private List<String> logs;
    //jsonobject to keep store of all logs
    private JSONObject mainJsonObject;
    int temp = 0;

    //CONSTRUCTOR
    Model() {
        entities = new ArrayList<>();
        listOfPersons = new ArrayList<>();
        listOfKeys  = new HashMap();

        logs = new ArrayList<>();


        mainJsonObject = new JSONObject().put("Logs", new JSONObject());
        LocalDateTime current = LocalDateTime.now();
        mainJsonObject.getJSONObject("Logs").put(current.toLocalDate().toString(),new JSONObject());
    }

    //logging system
    List<String> getLogsList() {
        return logs;
    }
    public void addLog(String log) {
        /*
        for debugging in case log file goes crazy
         */
        //System.out.println("\n"+logs.size()+"\n"+log);
        logs.add(log);
    }
    String getLatestLog() {
        return logs.get(logs.size()-1);
    }

    void getMainJsonObject() {
        //not used
    }

    void inputNewDateLog (String date) {
        //for each day create new date jsonobject

    }

    void inputLog(String date, String event, String time) {
        mainJsonObject.getJSONObject("Logs").getJSONObject(date).put(time,event);
//        System.out.println(mainJsonObject.toString(2)+ " \n");
    }

    //for creating entitys aka getAllEntities.
    Door createNewEntity(String name, int id, String location, int x, int y) {
        Door ent = null;
        for(Door d : entities) {
            if (d.getId() != id) {
                ent = new Door(name,id,location,x,y);
                ent.setState(false);
                ent.changeColor(1);
                entities.add(ent);
                return ent;
            }
        }
        return ent;
    }

    List<Door> getEntList () {
        return entities;
    }

    //for creating a person, now used for creating all
    void createNewPerson() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("files/students"));
            String line;
            int id_number = 0;
            while ((line = br.readLine()) != null) {
                Student student = new Student(line,++id_number);
                student.setPrivate_key();
                listOfPersons.add(student);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //returns list of all persons
    List<Student> getAllStudents () {
        return listOfPersons;
    }
    //returns all entities as list
    String getAllEntities() {
        StringBuilder sb = new StringBuilder();
        for (Door d : entities) {
            sb.append("Name: ")
                    .append(d.getName())
                    .append("\nID: ")
                    .append(d.getId())
                    .append("\nLocation: ")
                    .append(d.getLoc())
                    .append("\nState: ")
                    .append(d.getState())
                    .append("\nKey: ")
                    .append(d.getKey())
                    .append("\n\n");
        }
        return String.valueOf(sb);
    }

    //for searching or retreiving
    String searchStudentList (String id_name) {
        StringBuilder sb = new StringBuilder();
        if (id_name.matches("^[0-9]+$")) {
            for (Student s : listOfPersons) {
                if (s.getID() == Integer.parseInt(id_name)) {
                    return "Name: " + s.getName() + "\nID: " + s.getID();
                }
            }
        }
        for (Student s: listOfPersons) {
            if (s.getName().equals(id_name)) {
               sb.append("Name: ")
                       .append(s.getName())
                       .append("\nID: ")
                       .append(s.getID())
                       .append("\n\n");
            }
        }
        if (sb.length() == 0) {
            return "No such person exists";
        }
        return String.valueOf(sb);
    }

    //used to create personal keys
    Map<Long, Student> generateKeys () {
        for (Student s : listOfPersons) {
            s.setPrivate_key();
            listOfKeys.put(s.getPrivate_key(), s);
        }
        return listOfKeys;
    }

    //used to import all standard entities
    void importEntities(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            JSONTokener jsonTokener = new JSONTokener(new FileReader(file));

            System.out.println(jsonTokener.toString());
            String line;
            while ((line = br.readLine()) != null) {

         /*       String[] temp = line.split(",");
//                System.out.println(temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3] + " " + temp[4] + "\n");
                Door door = new Door(
                        temp[0],
                        Integer.parseInt(temp[1]),
                        temp[2],
                        Integer.parseInt(temp[3]),
                        Integer.parseInt(temp[4]));
                entities.add(door);*/
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
