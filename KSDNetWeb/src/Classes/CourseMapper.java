package Classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseMapper {
    public void createCourse(String courseid,String teacherid,String coursename,int projectnumber) throws SQLException{
        try{
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement(" INSERT INTO courses (course_id, teacher_id, name, number_projects) values (?, ?, ?, ?)") ;
            st.setString(1, courseid);
            st.setString(2, teacherid);
            st.setString(3, coursename);
            st.setInt(4, projectnumber);
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

    public ResultSet get_courseid(String coursename) throws  SQLException{
        try{
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("SELECT course_id FROM courses WHERE  name=?");
            st.setString(1, coursename);
            ResultSet rs = st.executeQuery();
            return  rs;
        }
        catch (Exception e){
            throw new SQLException("Could not obtain data.");

        }

    }

}
