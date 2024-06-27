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

public class ScheduleQueries {
    private static Connection connection;
    private static PreparedStatement addScheduleEntry;
    private static PreparedStatement getScheduleByStudent;
    private static PreparedStatement getStudentEnrollments;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement getScheduledStudentsByClass;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static PreparedStatement dropStudentScheduleByCourse;
    private static PreparedStatement dropScheduleByCourse;
    private static PreparedStatement updateScheduleEntry;
    private static ResultSet resultSet;
    
    public static void addScheduleEntry(ScheduleEntry entry) {
        connection = DBConnection.getConnection();
        try
        {
            addScheduleEntry = connection.prepareStatement("INSERT INTO APP.SCHEDULE (SEMESTER, COURSECODE, STUDENTID, STATUS, TIMESTAMP) VALUES (?,?,?,?,?)");
            addScheduleEntry.setString(1, entry.getSemester());
            addScheduleEntry.setString(2, entry.getCourseCode());
            addScheduleEntry.setString(3, entry.getStudentID());
            addScheduleEntry.setString(4, entry.getStatus());
            addScheduleEntry.setTimestamp(5, entry.getTimestamp());
            addScheduleEntry.executeUpdate();            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String studentID) {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
        try
        {
            getScheduleByStudent = connection.prepareStatement("SELECT SEMESTER, COURSECODE, STUDENTID, STATUS, TIMESTAMP FROM APP.SCHEDULE WHERE SEMESTER = (?) AND STUDENTID = (?)");
            getScheduleByStudent.setString(1, semester);
            getScheduleByStudent.setString(2, studentID);
            ResultSet resultSet = getScheduleByStudent.executeQuery();
            while (resultSet.next())
            {
                schedule.add(new ScheduleEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getTimestamp(5)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return schedule;
    }
    
    // same as getScheduleByStudent, but for ALL semesters
    public static ArrayList<ScheduleEntry> getStudentEnrollments(String studentID) {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
        try
        {
            getStudentEnrollments = connection.prepareStatement("SELECT SEMESTER, COURSECODE, STUDENTID, STATUS, TIMESTAMP FROM APP.SCHEDULE WHERE STUDENTID = (?)");
            getStudentEnrollments.setString(1, studentID);
            ResultSet resultSet = getStudentEnrollments.executeQuery();
            while (resultSet.next())
            {
                schedule.add(new ScheduleEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getTimestamp(5)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return schedule;
    }
    
    public static int getScheduledStudentCount(String currentSemester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> students = new ArrayList<String>();
        try
        {
            getScheduledStudentCount = connection.prepareStatement("SELECT STUDENTID FROM APP.SCHEDULE WHERE SEMESTER = (?) AND COURSECODE = (?)");
            getScheduledStudentCount.setString(1, currentSemester);
            getScheduledStudentCount.setString(2, courseCode);
            ResultSet resultSet = getScheduledStudentCount.executeQuery();
            while (resultSet.next())
            {
                students.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return students.size();
    }
    
    public static ArrayList<ScheduleEntry> getScheduledStudentsByClass(String semester, String courseCode) 
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> students = new ArrayList<ScheduleEntry>();
        try
        {
            getScheduledStudentsByClass = connection.prepareStatement("SELECT SEMESTER, COURSECODE, STUDENTID, STATUS, TIMESTAMP FROM APP.SCHEDULE WHERE SEMESTER = (?) AND COURSECODE = (?) AND STATUS = (?)");
            getScheduledStudentsByClass.setString(1, semester);
            getScheduledStudentsByClass.setString(2, courseCode);
            getScheduledStudentsByClass.setString(3, "Scheduled");
            ResultSet resultSet = getScheduledStudentsByClass.executeQuery();
            while (resultSet.next())
            {
                students.add(new ScheduleEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getTimestamp(5)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return students;
    }
    
    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByClass(String semester, String courseCode) 
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> students = new ArrayList<ScheduleEntry>();
        try
        {
            getWaitlistedStudentsByClass = connection.prepareStatement("SELECT SEMESTER, COURSECODE, STUDENTID, STATUS, TIMESTAMP FROM APP.SCHEDULE WHERE SEMESTER = (?) AND COURSECODE = (?) AND STATUS = (?)");
            getWaitlistedStudentsByClass.setString(1, semester);
            getWaitlistedStudentsByClass.setString(2, courseCode);
            getWaitlistedStudentsByClass.setString(3, "Waitlisted");
            ResultSet resultSet = getWaitlistedStudentsByClass.executeQuery();
            while (resultSet.next())
            {
                students.add(new ScheduleEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getTimestamp(5)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return students;
    }
    
    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropStudentScheduleByCourse = connection.prepareStatement("DELETE FROM APP.SCHEDULE WHERE SEMESTER = (?) AND COURSECODE = (?) AND STUDENTID = (?)");
            dropStudentScheduleByCourse.setString(1, semester);
            dropStudentScheduleByCourse.setString(2, courseCode);
            dropStudentScheduleByCourse.setString(3, studentID);
            dropStudentScheduleByCourse.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static void dropScheduleByCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropScheduleByCourse = connection.prepareStatement("DELETE FROM APP.SCHEDULE WHERE SEMESTER = (?) AND COURSECODE = (?)");
            dropScheduleByCourse.setString(1, semester);
            dropScheduleByCourse.setString(2, courseCode);
            dropScheduleByCourse.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static void updateScheduleEntry(ScheduleEntry entry)
    {
        connection = DBConnection.getConnection();
        try
        {
            updateScheduleEntry = connection.prepareStatement("UPDATE APP.SCHEDULE SET STATUS = (?) WHERE SEMESTER = (?) AND COURSECODE = (?) AND STUDENTID = (?) AND STATUS = (?) AND TIMESTAMP = (?)");
            updateScheduleEntry.setString(1, "Scheduled");
            updateScheduleEntry.setString(2, entry.getSemester());
            updateScheduleEntry.setString(3, entry.getCourseCode());
            updateScheduleEntry.setString(4, entry.getStudentID());
            updateScheduleEntry.setString(5, entry.getStatus());
            updateScheduleEntry.setTimestamp(6, entry.getTimestamp());
            updateScheduleEntry.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
