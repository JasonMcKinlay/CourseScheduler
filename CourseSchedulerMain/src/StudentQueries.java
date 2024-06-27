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

public class StudentQueries {
    private static Connection connection;
    private static PreparedStatement addStudent;
    private static PreparedStatement getAllStudents;
    private static PreparedStatement getStudent;
    private static PreparedStatement getStudentID;
    private static PreparedStatement dropStudent;
    private static ResultSet resultSet;
    
    public static void addStudent(StudentEntry student) {
        connection = DBConnection.getConnection();
        try
        {
            addStudent = connection.prepareStatement("INSERT INTO APP.STUDENT (STUDENTID, FIRSTNAME, LASTNAME) VALUES (?,?,?)");
            addStudent.setString(1, student.getStudentID());
            addStudent.setString(2, student.getFirstName());
            addStudent.setString(3, student.getLastName());
            addStudent.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<StudentEntry> getAllStudents() {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> students = new ArrayList<StudentEntry>();
        try
        {
            getAllStudents = connection.prepareStatement("SELECT STUDENTID, FIRSTNAME, LASTNAME FROM APP.STUDENT");
            ResultSet resultSet = getAllStudents.executeQuery();
            while(resultSet.next())
            {
                students.add(new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return students;
    }
    
    public static StudentEntry getStudent(String studentID) {
        connection = DBConnection.getConnection();
        StudentEntry student = null;
        try
        {
            getStudent = connection.prepareStatement("SELECT STUDENTID, FIRSTNAME, LASTNAME FROM APP.STUDENT WHERE STUDENTID = (?)");
            getStudent.setString(1, studentID);
            ResultSet resultSet = getStudent.executeQuery();
            while(resultSet.next())
            {
                student = new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        if (student != null) {
            return student;
        }
        else {
            return null;
        }
    }
    
    public static String getStudentID(String fullName)
    {
        connection = DBConnection.getConnection();
        String lastName = fullName.substring(0, fullName.indexOf(","));
        String firstName = fullName.substring(fullName.indexOf(" ")+1, fullName.length());
        String studentID = null;
        try
        {
            getStudentID = connection.prepareStatement("SELECT STUDENTID FROM APP.STUDENT WHERE FIRSTNAME = (?) AND LASTNAME = (?)");
            getStudentID.setString(1, firstName);
            getStudentID.setString(2, lastName);
            ResultSet resultSet = getStudentID.executeQuery();
            while(resultSet.next())
            {
                studentID = resultSet.getString(1);
            }
            
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return studentID;
        
    }
    
    public static void dropStudent(String studentID) {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> classes = ScheduleQueries.getStudentEnrollments(studentID);
        ArrayList<ScheduleEntry> waitlisted = new ArrayList<ScheduleEntry>();
        
        try
        {
            dropStudent = connection.prepareStatement("DELETE FROM APP.STUDENT WHERE STUDENTID = (?)");
            dropStudent.setString(1, studentID);
            dropStudent.executeUpdate();
            
            for (ScheduleEntry entry : classes)
            {
                ScheduleQueries.dropStudentScheduleByCourse(entry.getSemester(), studentID, entry.getCourseCode());
                waitlisted = ScheduleQueries.getWaitlistedStudentsByClass(entry.getSemester(), entry.getCourseCode());
                if (entry.getStatus().equals("Scheduled") && waitlisted.size() > 0)
                {
                    
                    //System.out.println(waitlisted);
                    ScheduleQueries.updateScheduleEntry(waitlisted.get(0));
                }
            }   
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }           
    }
}
