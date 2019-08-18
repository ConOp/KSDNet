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

    public ResultSet GetID(String group) throws SQLException{
        try {
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("SELECT student_id FROM groups WHERE group_id=?");
            st.setString(1,group);
            ResultSet Rs = st.executeQuery();
            return Rs;
        }catch(Exception e){
            throw new SQLException("Incorrect credentials");
        }
    }
}