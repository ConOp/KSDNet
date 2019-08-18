package Classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper {
    public void insertTotalgrade(String groupid, int totalgrade) throws SQLException {
        try {
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("UPDATE groups set total_grade=? where group_id=?");
            st.setInt(1, totalgrade);
            st.setString(2, groupid);
            st.executeUpdate();
            con.disconnect();
        } catch (Exception e) {
            throw new SQLException("Could not set total grade.");
        }
    }
    public String getCourseGrade(String studentid,String courseid) throws SQLException {
        try {
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("select total_grade from groups where student_id=? and course_id=?");
            st.setString(1, studentid);
            st.setString(2, courseid);
            ResultSet rs = st.executeQuery();
            String total_grade = "- ";
            while(rs.next()){
                if(rs.getString(1)!=null){ total_grade = rs.getString(1); }
            }
            return total_grade;
        } catch (Exception e) {
            throw new SQLException("Could not get total grade.");
        }
    }
    public ResultSet CheckExistingProject(String userid,String coursename,String projectid) throws  SQLException {
        try {
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("SELECT (SELECT group_id FROM groups INNER JOIN courses on groups.course_id = courses.course_id WHERE groups.student_id = ? AND courses.name = ?) FROM grade WHERE grade.project_id = ?");
            st.setString(1, userid);
            st.setString(2, coursename);
            st.setString(3, projectid);
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (Exception e) {
            throw new SQLException("Could not obtain data.");

        }
    }
}