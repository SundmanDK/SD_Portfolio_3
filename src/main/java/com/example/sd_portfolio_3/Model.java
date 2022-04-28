package com.example.sd_portfolio_3;

import java.util.ArrayList;
import java.util.Random;

class Model{
    Database db=new Database();
    Model(){
        //* Comment out this area if you want to save data between runs of the program.
        db.cmd("DROP TABLE IF EXISTS lecturer;");
        db.cmd("CREATE TABLE IF NOT EXISTS lecturer (name TEXT PRIMARY KEY);");
        db.cmd("DROP TABLE IF EXISTS course;");
        db.cmd("CREATE TABLE IF NOT EXISTS course (title TEXT PRIMARY KEY, participants INTEGER);");
        db.cmd("DROP TABLE IF EXISTS room;");
        db.cmd("CREATE TABLE IF NOT EXISTS room (name TEXT PRIMARY KEY, maxOccupants INTEGER);");
        db.cmd("DROP TABLE IF EXISTS timeSlot;");
        db.cmd("CREATE TABLE IF NOT EXISTS timeSlot (time STRING PRIMARY KEY);");

        db.cmd("DROP TABLE IF EXISTS lesson;");
        db.cmd("CREATE TABLE IF NOT EXISTS lesson (" +
                "courseID STRING REFERENCES course (title)," +
                " time STRING REFERENCES timeSlot (time)," +
                " PRIMARY KEY (courseID, time));");
        db.cmd("DROP TABLE IF EXISTS lecturerBooking;");
        db.cmd("CREATE TABLE IF NOT EXISTS lecturerBooking (" +
                "lecturerID STRING REFERENCES lecturer (name)," +
                "courseID String REFERENCES course (title)," +
                " PRIMARY KEY (lecturerID, courseID));");
        db.cmd("DROP TABLE IF EXISTS roomBooking;");
        db.cmd("CREATE TABLE IF NOT EXISTS roomBooking (" +
                "roomID STRING REFERENCES room (name)," +
                "courseID String REFERENCES course (title)," +
                " PRIMARY KEY (roomID, courseID));");

        initialValuesInTables();
        //*/
    }

    //Courses and lessons
    void addCourse(String title, int participants){
        db.cmd("INSERT INTO course (title, participants) VALUES ('"+title+"', "+participants+");");
    }
    boolean courseExists(String title){
        return db.query("SELECT title FROM course;","title").contains(title);
    }
    void createLesson(String course, String time){
        db.cmd("INSERT INTO lesson VALUES ('"+course+"', '"+time+"');");
    }
    ArrayList<String> getCourses(){
        return db.query("SELECT title FROM course", "title");
    }
    ArrayList<String> getTimeSlots(){
        return db.query("SELECT time FROM timeSlot","time");
    }
    ArrayList<String> getLessonsFromCourse(String course){
        return db.query("SELECT time FROM lesson WHERE courseID = '"+course+"'","time");
    }
    ArrayList<String> getLessonsAtTime(String time){
        return db.query("SELECT courseID FROM lesson WHERE time = '"+time+"';","courseID");
    }
    String getCourseInformation(String course){
        return db.query("SELECT participants FROM course WHERE title = '"+course+"'","participants").get(0);
    }

    //Lecturers
    void addLecturer(String name){
        db.cmd("INSERT INTO lecturer (name) VALUES ('"+name+"');");
    }
    boolean lecturerExists(String name){
        return db.query("SELECT name FROM lecturer;", "name").contains(name);
    }
    void bookLecturer(String lID, String cID){
        db.cmd("INSERT INTO lecturerBooking VALUES ('"+lID+"', '"+cID+"');");
    }
    boolean isLecturerAvailable(String lecturer, String time){
        return (db.query("SELECT lecturerID FROM lecturerBooking INNER JOIN lesson on lecturerBooking.courseID = lesson.courseID " +
                "WHERE lecturerID = '"+lecturer+"' AND lesson.time = '"+time+"'","lecturerID").size() == 0);
    }
    boolean lecturerBookingExists(String lecturer, String course){
        return db.query("SELECT lecturerID FROM lecturerBooking WHERE lecturerID = '"+lecturer+"' " +
                "AND courseID = '"+course+"'","lecturerID").size() > 0;
    }
    ArrayList<String> getLecturers(){
        return db.query("SELECT name FROM lecturer","name");
    }
    ArrayList<String> getAttachedLecturers(String courseTitle){
        return db.query("SELECT lecturerID FROM lecturerBooking WHERE courseID = '"+courseTitle+"';","lecturerID");
    }

    //Rooms
    void addRoom(String name, int maxOccupants){
        db.cmd("INSERT INTO room (name,maxOccupants) VALUES ('"+name+"',"+maxOccupants+");");
    }
    boolean roomExists(String room){
        return db.query("SELECT name FROM room;","name").contains(room);
    }
    void bookRoom(String rID, String cID){
        db.cmd("INSERT INTO roomBooking VALUES ('"+rID+"', '"+cID+"');");
    }
    ArrayList<String> getRooms(){
        return db.query("SELECT name FROM room","name");
    }
    ArrayList<String> getAttachedRooms(String courseTitle){
        return db.query("SELECT roomID FROM roomBooking WHERE courseID = '"+courseTitle+"';","roomID");
    }
    boolean isRoomAvailable(String room, String time){
        return !(db.query("SELECT roomID FROM roomBooking INNER JOIN lesson on roomBooking.courseID = lesson.courseID " +
                "WHERE roomID = '"+room+"' AND lesson.time = '"+time+"'","roomID").size() > 0);
    }
    boolean roomBookingExists(String room, String course){
        return db.query("SELECT roomID FROM roomBooking WHERE roomID = '"+room+"' AND courseID = '"+course+"'","roomID").size() > 0;
    }
    boolean canRoomFitCourse(String room, String course){
        return (db.query("SELECT name FROM room, course WHERE name = '"+room+"' AND title = '"+course+"' " +
                "AND maxOccupants + 10 >= participants","name").size() > 0);
    }
    boolean isRoomOverMaxOccupancy(String room, String course){
        return !(db.query("SELECT name FROM room, course WHERE name = '"+room+"' AND title = '"+course+"' " +
                "AND maxOccupants >= participants","name").size() > 0);
    }
    String getRoomInformation(String room){
        return db.query("SELECT maxOccupants FROM room WHERE name = '"+room+"'","maxOccupants").get(0);
    }

    void initialValuesInTables(){
        createTimeSlots();
        initialRooms();
        initialLecturers();
        initialCourses();
        System.out.println("Ignore any SQL errors before this they are a result of randomly generated initial inputs in the tables." +
                " Sometime identical values are generated");
    }
    void createTimeSlots(){
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String day : days){
            db.cmd("insert into timeSlot values ('"+day+" AM');");
            db.cmd("insert into timeSlot values ('"+day+" PM');");
        }
    }
    void initialLecturers(){
        String[] firstNames = {"Karina", "Per", "Mathias", "Johannes", "Jakob", "Anna", "Caroline"};
        String[] lastNames = {"Langkilde", "Sundman", "Voss", "Kristiansen", "Würggler", "Hansen", "Nielsen"};
        Random r = new Random();
        int amountOfLecturers = r.nextInt(5,11);
        for (int i = 0; i < amountOfLecturers; i++){
            addLecturer(firstNames[r.nextInt(0, firstNames.length)] +" "+ lastNames[r.nextInt(0, lastNames.length)]);
        }
    }
    void initialCourses(){
        String[] courses = {"Scientific Computing", "Software Development", "Linear Algebra", "Discrete Mathematics", "Essential Computing"};
        Random r = new Random();
        for (String course : courses){
            addCourse(course, r.nextInt(2,11)*10);
        }
    }
    void initialRooms(){
        String[] rooms = {"10.2-049","27.1-015","27.1-032","10.2-026","11.1-046","10.1-024"};
        Random r = new Random();
        for (String room : rooms){
            addRoom(room,r.nextInt(2,11)*10);
        }
    }
}


