package com.example.sd_portfolio_3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class View extends Application{
    private Model model = new Model();
    private Controller con = new Controller(model,this);
    private TextField textField = new TextField();
    private TextField numberField = new TextField();
    protected TextArea outputArea = new TextArea();
    protected TextArea monAM = new TextArea();
    protected TextArea monPM = new TextArea();
    protected TextArea tueAM = new TextArea();
    protected TextArea tuePM = new TextArea();
    protected TextArea wedAM = new TextArea();
    protected TextArea wedPM = new TextArea();
    protected TextArea thuAM = new TextArea();
    protected TextArea thuPM = new TextArea();
    protected TextArea friAM = new TextArea();
    protected TextArea friPM = new TextArea();
    public TextArea[] calendar = {monAM, monPM, tueAM, tuePM, wedAM, wedPM, thuAM, thuPM, friAM, friPM};
    ComboBox<String> lecturers = new ComboBox<>();
    ComboBox<String> courses = new ComboBox<>();
    ComboBox<String> rooms = new ComboBox<>();
    ComboBox<String> timeslots = new ComboBox<>();
    Button addLecturer = new Button("register lecturer");
    Button addRoom = new Button("create room");
    Button addCourse = new Button("Establish course");
    Button attachedRooms = new Button("Find attached room(s)");
    Button attachedLecturers = new Button("Find attached lecturer(s)");
    Button createLesson = new Button("Create lesson");
    Button bookLecturer = new Button("Book lecturer for course");
    Button bookRoom = new Button("Book room for course");

    public void setArea(TextArea a, String s){
        a.setText(s);}

    @Override
    public void start(Stage stage) {
        con.initArea(outputArea);
        textField.setPromptText("Enter text");
        numberField.setPromptText("Enter numbers");
        outputArea.setPromptText("Output messages");
        outputArea.setPrefHeight(100);  //sets height of the TextArea to 100 pixels
        outputArea.setPrefWidth(400);   //Sets width of the TextArea to 400 pixels
        for (TextArea day : calendar){
            day.setPrefHeight(100);  //sets height of the TextArea to 400 pixels
            day.setPrefWidth(100);
        }
        HBox topBar = new HBox(courses, lecturers, rooms, timeslots);
        HBox outputBar = new HBox(numberField, outputArea);
        HBox addBar = new HBox(addLecturer, addRoom, addCourse);
        HBox findBar = new HBox(attachedLecturers, attachedRooms);
        HBox bookingBar = new HBox(createLesson, bookLecturer, bookRoom);
        HBox AMBar = new HBox(monAM, tueAM, wedAM, thuAM, friAM);
        HBox PMBar = new HBox(monPM, tuePM, wedPM, thuPM, friPM);
        Label days = new Label("        Monday                  Tuesday              Wednesday               Thursday                 Friday");
        VBox root = new VBox(topBar, textField, outputBar, addBar, findBar, bookingBar,days, AMBar, PMBar);
        lecturers.getItems().addAll(model.getLecturers());
        courses.getItems().addAll(model.getCourses());
        rooms.getItems().addAll(model.getRooms());
        timeslots.getItems().addAll(model.getTimeSlots());
        rooms.setOnAction(e->con.getRoomInformation(rooms.getValue(), outputArea));
        courses.setOnAction(e->con.getCourseInformation(courses.getValue(), outputArea));
        addLecturer.setOnAction(e->con.addLecturer(textField.getText(), outputArea));
        addRoom.setOnAction(e->con.addRoom(textField.getText(), numberField.getText(), outputArea));
        addCourse.setOnAction(e->con.addCourse(textField.getText(), numberField.getText(), outputArea));
        attachedLecturers.setOnAction(e->con.findAttachedLecturers(courses.getValue(), outputArea));
        attachedRooms.setOnAction(e->con.findAttachedRooms(courses.getValue(), outputArea));
        createLesson.setOnAction(e->con.createLesson(courses.getValue(), timeslots.getValue(), outputArea));
        bookLecturer.setOnAction(e->con.attachLecturer(lecturers.getValue(), courses.getValue(), outputArea));
        bookRoom.setOnAction(e->con.attachRoom(rooms.getValue(), courses.getValue(), outputArea));
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Course Management");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}


