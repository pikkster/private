import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AccessPoints.*;
import Person.*;
import org.json.JSONArray;
import org.json.JSONObject;

class Model {

    //list of all entities
    private List<Door> entities;
    //list of all persons
    private List<Student> listOfPersons;
    //list of all keys for future usage
    private Map<Long, Student> listOfKeys;
    //jsonobject to keep store of all logs
    private JSONObject mainJsonObject;
    private String tempJSONLOG;

    //CONSTRUCTOR
    Model() {
        entities = new ArrayList<>();
        listOfPersons = new ArrayList<>();
        listOfKeys  = new HashMap();

        mainJsonObject = new JSONObject().put("Logs", new JSONObject());
        LocalDateTime currentTime = LocalDateTime.now();
        mainJsonObject.getJSONObject("Logs").put(currentTime.toLocalDate().toString(), new JSONObject());

    }

    public void addLog(JSONObject timeLog, String time) {
        //System.out.println(timeLog.toString(2));

        tempJSONLOG = "Time: " + time + "\n" +
                "Door: "+ timeLog.get("Door").toString() + "\n" +
                "Door-ID: "+ timeLog.get("Door-ID").toString() + "\n" +
                "Door-key: "+ timeLog.get("Door-key").toString() + "\n" +
                "Granted: "+ timeLog.get("Granted").toString() + "\n" +
                "Name: "+ timeLog.get("Name").toString() + "\n" +
                "ID: "+ timeLog.get("ID").toString() + "\n" +
                "Private Key: "+ timeLog.get("Private Key").toString() + "\n\n" +
                "//////////////////////////////////////////\n\n";
    }
    public String getLatestJSONLOG () {
        return tempJSONLOG;
    }
    void inputNewDateLog (String date) {
        //not used at the moment, to be...
    }

    void inputLog(String date, Student event, String time, Door door, boolean granted) {

        mainJsonObject.getJSONObject("Logs").getJSONObject(date).put(time,new JSONObject());

        JSONObject timeLog = mainJsonObject.getJSONObject("Logs").getJSONObject(date).getJSONObject(time);
        timeLog.put("Door", door.getName());
        timeLog.put("Door-ID", door.getId());
        timeLog.put("Door-key", door.getKey());
        timeLog.put("Granted", granted);
        timeLog.put("Name",event.getName());
        timeLog.put("ID", event.getID());
        timeLog.put("Private Key", event.getPrivate_key());
        addLog(timeLog, time);
        /*
            FOR DEBUGGING JSON DATA
         */
        //System.out.println(mainJsonObject.toString(2)+ " \n");
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
    public void importEntities(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                //System.out.println(temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3] + " " + temp[4] + "\n");
                Door door = new Door(temp[0],
                        Integer.parseInt(temp[1]),
                        temp[2],
                        Integer.parseInt(temp[3]),
                        Integer.parseInt(temp[4]));
                entities.add(door);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
