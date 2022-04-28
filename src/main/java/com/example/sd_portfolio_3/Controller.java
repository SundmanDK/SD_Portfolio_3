package com.example.sd_portfolio_3;

import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class Controller{
    Model model;
    View view;
    Controller(Model model, View view){
        this.model = model;
        this.view = view;
    }
    void initArea(TextArea output){
        String toarea = "";
        view.setArea(output, toarea);
        updateSchedule();
    }

    void getRoomInformation(String room, TextArea output){
        String roomInfo = model.getRoomInformation(room);
        view.setArea(output, "Max Occupants: "+ roomInfo);
    }

    void getCourseInformation(String course, TextArea output){
        String courseInfo = model.getCourseInformation(course);
        view.setArea(output, "Expected participants: "+ courseInfo);
    }

    void addLecturer(String name, TextArea output){
        if(model.lecturerExists(name) || name.equals("")){
            view.setArea(output, "Cannot insert lecturer: \'"+ name +"\'");
        } else {
            model.addLecturer(name);
            view.lecturers.getItems().add(name);
            view.setArea(output,"Registered lecturer: \'"+ name +"\'");
        }
    }
    void addRoom(String room, String maxOccu, TextArea output){
        int maxOccupants;
        try{
            maxOccupants = Integer.parseInt(maxOccu);
        } catch (Exception e){
            view.setArea(output, "Maximum occupants provided was not a number");
            return;
        }
        if(model.roomExists(room) || room.equals("")){
            view.setArea(output,"Cannot insert lecturer (repeat) "+room);
        } else {
            model.addRoom(room, maxOccupants);
            view.rooms.getItems().add(room);
            view.setArea(output,"Created room: "+ room +", with max occupants: "+ maxOccupants);
        }
    }
    void addCourse(String title, String partici, TextArea output){
        int participants;
        try{
            participants = Integer.parseInt(partici);
        } catch (Exception e){
            view.setArea(output, "Expected participants provided was not a number");
            return;
        }
        if(model.courseExists(title) || title.equals("")){
            view.setArea(output,"Cannot insert lecturer (repeat) "+title);
        } else {
            model.addRoom(title, participants);
            view.courses.getItems().add(title);
            view.setArea(output,"Established course: "+ title +", with expected participants: "+ participants);
        }
    }
    public void findAttachedRooms(String courseTitle, TextArea output){
        ArrayList<String> rooms = model.getAttachedRooms(courseTitle);
        if(rooms.size() > 0) view.setArea(output,"Room(s): "+ rooms);
        else view.setArea(output,"No Room");
    }

    public void findAttachedLecturers(String courseTitle, TextArea output) {
        ArrayList<String> lecturers = model.getAttachedLecturers(courseTitle);
        if(lecturers.size() > 0) view.setArea(output, "Lecturer(s): "+ lecturers);
        else view.setArea(output, "No Lecturer");
    }

    public void createLesson(String course, String time, TextArea output){
        if (course == null || time == null){
            view.setArea(output, "Missing information");
            return;
        }
        ArrayList<String> rooms = model.getAttachedRooms(course);
        for (String room : rooms) {
            if (!model.isRoomAvailable(room, time)){
                view.setArea(output, "Room already booked");
                return;
            }
        }
        ArrayList<String> lecturers = model.getAttachedLecturers(course);
        for (String lecturer : lecturers) {
            if (!model.isLecturerAvailable(lecturer, time)){
                view.setArea(output, "Lecturer already booked");
                return;}
        }

        model.createLesson(course, time);
        view.setArea(view.outputArea,"Created: "+ course +" lesson, on: "+ time);
        updateSchedule();
    }

    public void attachLecturer(String lecturer, String course, TextArea output) {
        if (course == null || lecturer == null){
            view.setArea(output, "Missing information");
            return;
        }
        if (model.lecturerBookingExists(lecturer, course)){
            view.setArea(output, "This lecturer is already booked for this course");
            return;
        }

        ArrayList<String> lessons = model.getLessonsFromCourse(course);
        for (String time : lessons) {
            if (!model.isLecturerAvailable(lecturer, time)) {
                view.setArea(output, "Lecturer already booked");
                return;
            }
        }

        model.bookLecturer(lecturer, course);
        view.setArea(view.outputArea,"Booked: "+ lecturer +", for: "+ course);
    }

    public void attachRoom(String room, String course, TextArea output){
        if (room == null || course == null){
            view.setArea(output, "Missing information");
            return;
        }
        if (model.roomBookingExists(room, course)){
            view.setArea(output, "This room is already booked for this course");
            return;
        }
        ArrayList<String> lessons = model.getLessonsFromCourse(course);
        for (String time : lessons) {
            if (!model.isRoomAvailable(room, time)) {
                view.setArea(output, "Room already booked");
                return;
            }
            System.out.println("hello");
        }
        if (!model.canRoomFitCourse(room, course)){
            view.setArea(output, "Room is not big enough");
            return;
        }

        model.bookRoom(room, course);
        if (model.isRoomOverMaxOccupancy(room, course)) {
            view.setArea(output, "Booked: "+ room +", for: "+ course+"\n" +
                    "This room was made to handle: " + model.getRoomInformation(room) + " occupants,\n" +
                    "the course expects: " + model.getCourseInformation(course) + " participants");
        } else {
            view.setArea(output, "Booked: " + room + ", for: " + course);
        }
    }

    public void updateSchedule(){
        ArrayList<String> timeSlots = model.getTimeSlots();
        for (int i = 0; i < timeSlots.size(); i++){
            ArrayList<String> lessons = model.getLessonsAtTime(timeSlots.get(i));
            String l = "";
            for (String lesson : lessons){
                l += lesson +"\n";
            }
            view.setArea(view.calendar[i], l);
        }
    }
}
