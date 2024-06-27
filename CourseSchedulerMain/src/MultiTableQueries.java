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
import java.util.Dictionary;

public class MultiTableQueries {
    private static Connection connection;
    private static PreparedStatement getAllClassCodes;
    private static PreparedStatement getAllClassDescriptions;
    private static PreparedStatement getScheduledStudentIDsByClass;
    private static PreparedStatement getScheduledStudentsByClass;
    private static PreparedStatement getWaitlistedStudentIDsByClass;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static ResultSet resultSet1;
    private static ResultSet resultSet2;
    
    public static ArrayList<ClassDescription> getAllClassDescriptions(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<String>();
        ArrayList<Integer> seats = new ArrayList<Integer>();
        ArrayList<ClassDescription> descriptions = new ArrayList<ClassDescription>();
        
        try
        {
            getAllClassCodes = connection.prepareStatement("SELECT COURSECODE, SEATS FROM APP.CLASS WHERE SEMESTER = (?)");
            getAllClassCodes.setString(1, semester);
            ResultSet resultSet1 = getAllClassCodes.executeQuery();
            while (resultSet1.next())
            {
                courseCodes.add(resultSet1.getString(1));
                seats.add(resultSet1.getInt(2));
            }
            
            getAllClassDescriptions = connection.prepareStatement("SELECT DESCRIPTION FROM APP.COURSE WHERE COURSECODE = (?)");
            for (int i = 0; i < courseCodes.size(); i++)
            {
                getAllClassDescriptions.setString(1, courseCodes.get(i));
                ResultSet resultSet2 = getAllClassDescriptions.executeQuery();
                while (resultSet2.next())
                {
                    descriptions.add(new ClassDescription(courseCodes.get(i), resultSet2.getString(1), seats.get(i)));
                }
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return descriptions;
    }
    
    public static ArrayList<StudentEntry> getScheduledStudentsByClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> studentIDs = new ArrayList<String>();
        ArrayList<StudentEntry> students = new ArrayList<StudentEntry>();
        try
        {
            getScheduledStudentIDsByClass = connection.prepareStatement("SELECT STUDENTID FROM APP.SCHEDULE WHERE SEMESTER = (?) AND COURSECODE = (?) AND STATUS = (?)");
            getScheduledStudentIDsByClass.setString(1, semester);
            getScheduledStudentIDsByClass.setString(2, courseCode);
            getScheduledStudentIDsByClass.setString(3, "Scheduled");
            ResultSet resultSet1 = getScheduledStudentIDsByClass.executeQuery();
            while (resultSet1.next())
            {
                studentIDs.add(resultSet1.getString(1));
            }
            
            getScheduledStudentsByClass = connection.prepareStatement("SELECT STUDENTID, FIRSTNAME, LASTNAME FROM APP.STUDENT WHERE STUDENTID = (?)");
            for (String studentID : studentIDs)
            {
                getScheduledStudentsByClass.setString(1, studentID);
                ResultSet resultSet2 = getScheduledStudentsByClass.executeQuery();
                while (resultSet2.next())
                {
                    students.add(new StudentEntry(resultSet2.getString(1), resultSet2.getString(2), resultSet2.getString(3)));
                }
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return students;
    }
    
    public static ArrayList<StudentEntry> getWaitlistedStudentsByClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> studentIDs = new ArrayList<String>();
        ArrayList<StudentEntry> students = new ArrayList<StudentEntry>();
        try
        {
            getWaitlistedStudentIDsByClass = connection.prepareStatement("SELECT STUDENTID FROM APP.SCHEDULE WHERE SEMESTER = (?) AND COURSECODE = (?) AND STATUS = (?)");
            getWaitlistedStudentIDsByClass.setString(1, semester);
            getWaitlistedStudentIDsByClass.setString(2, courseCode);
            getWaitlistedStudentIDsByClass.setString(3, "Waitlisted");
            ResultSet resultSet1 = getWaitlistedStudentIDsByClass.executeQuery();
            while (resultSet1.next())
            {
                studentIDs.add(resultSet1.getString(1));
            }
            
            getWaitlistedStudentsByClass = connection.prepareStatement("SELECT STUDENTID, FIRSTNAME, LASTNAME FROM APP.STUDENT WHERE STUDENTID = (?)");
            for (String studentID : studentIDs)
            {
                getWaitlistedStudentsByClass.setString(1, studentID);
                ResultSet resultSet2 = getWaitlistedStudentsByClass.executeQuery();
                while (resultSet2.next())
                {
                    students.add(new StudentEntry(resultSet2.getString(1), resultSet2.getString(2), resultSet2.getString(3)));
                }
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return students;
    }
    
}
