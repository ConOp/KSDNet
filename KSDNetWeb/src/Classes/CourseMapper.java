package Classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseMapper {
    public void createCourse(String courseid,String teacherid,String coursename,int projectnumber,int groupmembers) throws SQLException{
        try{
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement(" INSERT INTO courses (course_id, teacher_id, name, number_projects,groupmembers) values (?, ?, ?, ?,?)") ;
            st.setString(1, courseid);
            st.setString(2, teacherid);
            st.setString(3, coursename);
            st.setInt(4, projectnumber);
            st.setInt(5, groupmembers);
            st.executeUpdate();
            st.close();
            connector.disconnect();

        }
        catch (Exception e){
            throw new SQLException("Could not create course.");
        }

    }
    public void deleteCourse(String courseid)throws  SQLException{
        try{
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement(" DELETE  FROM courses  WHERE course_id= ?") ;
            st.setString(1, courseid);
            st.executeUpdate();
            st.close();
            connector.disconnect();

        }
        catch (Exception e){
            throw new SQLException("Could not delete course");
        }
    }

    public String get_courseid(String coursename) throws  SQLException{
        try{
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("SELECT course_id FROM courses WHERE  name=?");
            st.setString(1, coursename);
            ResultSet rs = st.executeQuery();
            rs.next();
            return  rs.getString(1);
        }
        catch (Exception e){
            throw new SQLException("Could not obtain data.");

        }

    }
    public ResultSet get_allcoursesofteacher(String teacherid) throws  SQLException {
        try {
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("SELECT name FROM courses WHERE  teacher_id=?");
            st.setString(1, teacherid);
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (Exception e) {
            throw new SQLException("Could not obtain data.");

        }
    }
    public ResultSet get_allcourses() throws  SQLException {
        try {
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("SELECT * FROM courses");
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (Exception e) {
            throw new SQLException("Could not obtain data.");

        }
    }

    public ResultSet groupmembers(String coursename) throws  SQLException {
        try {
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("SELECT groupmembers FROM courses WHERE courses.name = ?;");
            st.setString(1, coursename);
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (Exception e) {
            throw new SQLException("Could not obtain data.");

        }
    }

    public ResultSet course_info(String coursename) throws  SQLException {
        try {
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("SELECT course_id, courses.name, teachers.surname, number_projects, groupmembers FROM COURSES " +
                    "INNER JOIN teachers on courses.teacher_id = teachers.teacher_id where courses.name=?;");
            st.setString(1, coursename);
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (Exception e) {
            throw new SQLException("Could not obtain data.");

        }
    }

     public int getnumofprojects(String courseid) throws  SQLException{
            try{
                Dbconnector connector = new Dbconnector();
                PreparedStatement st = connector.connect().prepareStatement("SELECT number_projects FROM courses WHERE  course_id=?");
                st.setString(1,courseid);
                ResultSet rs = st.executeQuery();
                rs.next();
                return  rs.getInt(1);
            }
            catch (Exception e){
                throw new SQLException("Could not obtain data.");

            }
    }

}
