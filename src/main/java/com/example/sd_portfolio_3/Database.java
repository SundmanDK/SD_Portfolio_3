package com.example.sd_portfolio_3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

class Database {
    Connection conn = null;
    Database(){
        if(conn==null)open();
    }

    public void open(){
        try {
            String url = "jdbc:sqlite:CourseManagementdb.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("cannot open");
            if (conn != null) close();
        };
    }
    public void close(){
        try {
            if (conn != null) conn.close();
        } catch (SQLException e ) {
            System.out.println("cannot close");
        }
        conn=null;
    }
    public void cmd(String sql){
        if(conn==null)open();
        if(conn==null){System.out.println("No connection");return;}
        Statement statement=null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e ) {
            System.out.println("Error in statement "+sql);
        }
        try {
            if (statement != null) { statement.close(); }
        } catch (SQLException e ) {
            System.out.println("Error in statement "+sql);
        }
    }

    public ArrayList<String> query(String query,String field){
        ArrayList<String> result = new ArrayList<>();
        if(conn==null) open();
        if(conn==null){
            System.out.println("No connection");
            return result;
        }
        Statement stmt=null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString(field);
                result.add(name);
            }
        } catch (SQLException e ) {
            System.out.println(e);
            System.out.println("Error in statement "+query+" "+field);
        }
        try {
            if (stmt != null) { stmt.close(); }
        } catch (SQLException e ) {
            System.out.println("Error in statement "+query+" "+field);
        }
        return result;
    }
}

