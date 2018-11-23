import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import AccessPoints.*;
import Person.*;
import org.json.JSONObject;
import org.json.JSONWriter;

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

//    keep track of current date
    private LocalDateTime currentTime;

    private Stack<JSONObject> logStack;

    //CONSTRUCTOR
    Model() {
        entities = new ArrayList<>();
        listOfPersons = new ArrayList<>();
        listOfKeys  = new HashMap();

        mainJSONLog = new JSONObject().put("Logs", new JSONObject());
        currentTime = LocalDateTime.now();
        mainJSONLog.getJSONObject("Logs").put(currentTime.toLocalDate().toString(), new JSONObject());
        logStack = new Stack<>();
    }

    private void addLog(JSONObject timeLog, String time) {
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
        /*JSONObject test = new JSONObject().put("Time",time)
                .put("Door",timeLog.getString("Door"))
                .put("Door-ID",timeLog.getInt("Door-ID"))
                .put("Granted", timeLog.getBoolean("Granted"))
                .put("Name",timeLog.getString("Name"))
                .put("ID",timeLog.getInt("ID"))
                .put("Private Key",timeLog.getLong("Private Key"));*/


    }

    String getLatestJSONLOG () {
        return outputJSONLOG;
    }
//    @logStack temporary location for logs to be written to log file
//    Clear @logStack after each request to write to file
//
    void addToLogStack (JSONObject timeLog) {
            logStack.push(timeLog);
    }
    void clearLogStack () {
        logStack.clear();
    }

    private void appendLogToFile(File file) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(file,true));
            for (JSONObject js : logStack) {
                out.write(js.toString(2) + "\n");
            }
//            out.write(getLatestJSONLOG());
            out.close();
        } catch (Exception we) {
            //
        }
    }

    private File searchLogFiles(String date) {
        //search log folder to find .json with correct date and then call appendToLogFile()
        File dir = new File("logs");
        File[] matches = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(date);
            }
        });
        if (matches != null) {
            for (File f : matches) {
                System.out.println(f.toString());
                return f;
            }
        } else {
            System.out.println("error");
        }
        return null;
    }

    void inputNewDateLog () {
        try {

            File existingLogFile = searchLogFiles(String.valueOf(getCurrentTime().toLocalDate()));
            if (existingLogFile == null) {
                String fileSeparator = System.getProperty("file.separator");
                String newLogFile = "logs" + fileSeparator + String.valueOf(getLocalDate());

                File logFile = new File(newLogFile);
                if (logFile.createNewFile()) {
                    appendLogToFile(logFile);
                }
            } else {
                appendLogToFile(existingLogFile);
            }
        } catch (Exception ee) {
//
        }
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
        timeLog.put("Time",time);
        addLog(timeLog, time);
        addToLogStack(timeLog);
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
    void createPersonsAtStart() {
        String fileSeparator = System.getProperty("file.separator");
        try {
            String content = new String(Files.readAllBytes(Paths
                    .get("files" + fileSeparator + "JSON_students.json")));
            JSONObject jsonObject = new JSONObject(content);

            String[] ID = getNames(jsonObject.getJSONObject("Student"));

            for(String s : ID) {
                Student student = new Student(
                        jsonObject.getJSONObject("Student").getJSONObject(s).getString("Name"),
                        jsonObject.getJSONObject("Student").getJSONObject(s).getInt("ID"));
                listOfPersons.add(student);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
/*        try {
//            createJSONstudents();
        } catch (Exception wewe) {
            //
        }*/
    }

//    only used for creation of the initial json_students.json file
    private void createJSONstudents () throws Exception{
        String fileSeparator = System.getProperty("file.separator");
        String pathToLogFolder = "files" + fileSeparator + "JSON_students.json";

        File file = new File(pathToLogFolder);
        if (file.createNewFile()) {
//            System.out.println("File created");
            BufferedWriter out = new BufferedWriter(
                    new FileWriter("files" + fileSeparator + "JSON_students.json",true));

            new JSONWriter(out)
                    .object()
                    .key("Student")
                    .object()
                    .endObject()
                    .endObject();
            out.close();
        } else {
//            System.out.println("EXIST");
        }

        String content = new String(Files.readAllBytes(Paths
                .get("files" + fileSeparator + "JSON_students.json")));
        JSONObject jsonObject  = new JSONObject(content);
//        System.out.println(jsonObject.toString(2));

        JSONObject student = jsonObject.getJSONObject("Student");
        for (Student s : listOfPersons) {
            student.put(Integer.toString(s.getID()),new JSONObject());
            student.getJSONObject(Integer.toString(s.getID()))
                    .put("Name",s.getName())
                    .put("ID", s.getID())
                    .put("Key", s.getPrivate_key());
        }

        /*BufferedWriter out = new BufferedWriter(
                new FileWriter("files" + fileSeparator + "JSON_students.json"));
        out.write(jsonObject.toString(2));
        out.close();*/
//        System.out.println(jsonObject.toString(2));

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

    void setCurrentDate() {
        currentTime = LocalDateTime.now();
        LocalDate date = currentTime.toLocalDate();
    }

    public LocalDate getLocalDate() {
        return currentTime.toLocalDate();
    }
    public LocalDateTime getCurrentTime (){
        return currentTime;
    }

}
