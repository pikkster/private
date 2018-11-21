import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AccessPoints.*;
import Person.*;
import org.json.JSONObject;

import static org.json.JSONObject.getNames;

class Model {

    //list of all entities
    private List<Door> entities;
    //list of all persons
    private List<Student> listOfPersons;
    //list of all keys for future usage
    private Map<Long, Student> listOfKeys;
    //jsonobject to keep store of all logs
    private JSONObject mainJSONLog;
    private String outputJSONLOG;
    private JSONObject entitiesJSONdata;

    //CONSTRUCTOR
    Model() {
        entities = new ArrayList<>();
        listOfPersons = new ArrayList<>();
        listOfKeys  = new HashMap();

        mainJSONLog = new JSONObject().put("Logs", new JSONObject());
        LocalDateTime currentTime = LocalDateTime.now();
        mainJSONLog.getJSONObject("Logs").put(currentTime.toLocalDate().toString(), new JSONObject());

    }

    public void addLog(JSONObject timeLog, String time) {
        //System.out.println(timeLog.toString(2));

        outputJSONLOG = "Time: " + time + "\n" +
                "Door: "+ timeLog.get("Door").toString() + "\n" +
                "Door-ID: "+ timeLog.get("Door-ID").toString() + "\n" +
                "Door-key: "+ timeLog.get("Door-key").toString() + "\n" +
                "Granted: "+ timeLog.get("Granted").toString() + "\n" +
                "Name: "+ timeLog.get("Name").toString() + "\n" +
                "ID: "+ timeLog.get("ID").toString() + "\n" +
                "Private Key: "+ timeLog.get("Private Key").toString() + "\n\n" +
                "//////////////////////////////////////////\n\n";
    }
    String getLatestJSONLOG () {
        return outputJSONLOG;
    }
    void appendLogToFile (String date) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter("files/temp_logs",true));
            out.write("test String\na$$\n" + date);
            out.close();
        } catch (Exception we) {
            //
        }
    }

    private void searchLogFiles() {
        //searcg log folder to find .json with correct date and then call appendToLogFile()
    }

    void inputNewDateLog (String date) {
        //not used at the moment, to be...

        appendLogToFile(date);
    }

    void inputLog(String date, Student event, String time, Door door, boolean granted) {

        mainJSONLog.getJSONObject("Logs").getJSONObject(date).put(time,new JSONObject());

        JSONObject timeLog = mainJSONLog.getJSONObject("Logs").getJSONObject(date).getJSONObject(time);
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
        //System.out.println(mainJSONLog.toString(2)+ " \n");
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
    String createAllStudentsString () {
        StringBuilder sb = new StringBuilder();
        for (Student s : getAllStudents()) {
            sb.append("Name: ")
                    .append(s.getName())
                    .append("\nID: ")
                    .append(s.getID())
                    .append("\n-------------------\n");
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
    //read and create json entities file
    void jsonEntities (String file) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(file)));
            entitiesJSONdata = new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        createEntitiesAtStart();
    }
    //create entities at start
    private void createEntitiesAtStart () {
        String[] jsonData = getNames(entitiesJSONdata
                .getJSONObject("Entities")
                .getJSONObject("Doors"));
        for(String s : jsonData) {
            JSONObject newDoor = entitiesJSONdata.getJSONObject("Entities")
                    .getJSONObject("Doors").getJSONObject(s);

            Door door = new Door(
                    newDoor.getString("Name"),
                    newDoor.getInt("ID"),
                    newDoor.getString("Location"),
                    newDoor.getInt("X-pos"),
                    newDoor.getInt("Y-pos"));
            entities.add(door);
        }
    }

}
