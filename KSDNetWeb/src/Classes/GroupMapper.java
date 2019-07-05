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
    public int getCourseGrade(String studentid,String courseid) throws SQLException {
        try {
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("select total_grade from groups where student_id=? and course_id=?");
            st.setString(1, studentid);
            st.setString(2, courseid);
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            throw new SQLException("Could not get total grade.");
        }
    }
}