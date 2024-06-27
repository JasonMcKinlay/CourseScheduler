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

public class CourseQueries {
    private static Connection connection;
    private static PreparedStatement addCourse;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getDescription;
    private static ResultSet resultset;
    
    public static void addCourse(CourseEntry course) {
        connection = DBConnection.getConnection();
        try
        {
            addCourse = connection.prepareStatement("INSERT INTO APP.COURSE (COURSECODE, DESCRIPTION) VALUES (?,?)");
            addCourse.setString(1, course.getCourseCode());
            addCourse.setString(2, course.getDescription());
            addCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<String> getAllCourseCodes() {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<String>();
        try
        {
            getAllCourseCodes = connection.prepareStatement("SELECT COURSECODE, DESCRIPTION FROM APP.COURSE");
            ResultSet resultSet = getAllCourseCodes.executeQuery();
            while(resultSet.next())
            {
                courseCodes.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return courseCodes;
    }
    
    public static String getDescription(ClassEntry classEntry) {
        connection = DBConnection.getConnection();
        String description = "";
        
        try
        {
            getDescription = connection.prepareStatement("SELECT DESCRIPTION FROM APP.COURSE WHERE COURSECODE = (?)");
            getDescription.setString(1, classEntry.getCourseCode());
            ResultSet resultSet = getDescription.executeQuery();
            while (resultSet.next())
            {
                description = resultSet.getString(1);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return description;
    }
}
