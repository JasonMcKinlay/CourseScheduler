/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jason
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassQueries {
    private static Connection connection;
    private static PreparedStatement addClass;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getClassSeats;
    private static PreparedStatement dropClass;
    private static ResultSet resultSet;
    
    public static void addClass(ClassEntry classEntry) {
        connection = DBConnection.getConnection();
        try
        {
            addClass = connection.prepareStatement("INSERT INTO APP.CLASS (SEMESTER, COURSECODE, SEATS) VALUES (?,?,?)");
            addClass.setString(1, classEntry.getSemester());
            addClass.setString(2, classEntry.getCourseCode());
            addClass.setInt(3, classEntry.getSeats());
            addClass.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<String> getAllCourseCodes(String semester) {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<String>();
        try
        {
            getAllCourseCodes = connection.prepareStatement("SELECT SEMESTER, COURSECODE, SEATS FROM APP.CLASS WHERE SEMESTER = (?)");
            getAllCourseCodes.setString(1, semester);
            ResultSet resultSet = getAllCourseCodes.executeQuery();
            while(resultSet.next())
            {
                courseCodes.add(resultSet.getString(2));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return courseCodes;
    }
    
    public static ArrayList<ClassEntry> getAllClasses(String semester) {
        connection = DBConnection.getConnection();
        ArrayList<ClassEntry> classes = new ArrayList<ClassEntry>();
        try
        {
            getAllCourseCodes = connection.prepareStatement("SELECT SEMESTER, COURSECODE, SEATS FROM APP.CLASS WHERE SEMESTER = (?)");
            getAllCourseCodes.setString(1, semester);
            ResultSet resultSet = getAllCourseCodes.executeQuery();
            while(resultSet.next())
            {
                classes.add(new ClassEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return classes;
    }
    
    public static int getClassSeats(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        int seats = 0;
        try
        {
            getClassSeats = connection.prepareStatement("SELECT SEATS FROM APP.CLASS WHERE SEMESTER = (?) AND COURSECODE = (?)");
            getClassSeats.setString(1, semester);
            getClassSeats.setString(2, courseCode);
            ResultSet resultSet = getClassSeats.executeQuery();
            while (resultSet.next())
            {
                seats = resultSet.getInt(1);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return seats;
    }
    
    public static void dropClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropClass = connection.prepareStatement("DELETE FROM APP.CLASS WHERE SEMESTER = (?) AND COURSECODE = (?)");
            dropClass.setString(1, semester);
            dropClass.setString(2, courseCode);
            dropClass.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
