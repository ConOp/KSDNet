package Classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper {
    //Inserts total grade to a specific group
    public void insertTotalgrade(String groupid, int totalgrade) throws SQLException {
        try {
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("UPDATE groups set total_grade=? where group_id=?");
            st.setInt(1, totalgrade);
            st.setString(2, groupid);
            st.executeUpdate();
            st.close();
            con.disconnect();
        } catch (Exception e) {
            throw new SQLException("Could not set total grade.");
        }
    }
    //Returns the grade of a student at a specific course
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
            st.close();
            con.disconnect();
            return total_grade;
        } catch (Exception e) {
            throw new SQLException("Could not get total grade.");
        }
    }
    //Returns the groupid given the userid,coursename and projectid
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
    //Assigns multiple students to a group
    public void group_assign(String groupid,String userid,String[] guserids,String coursename) throws SQLException {
        try {
            String statement = "INSERT INTO groups(group_id,student_id,course_id) VALUES ('" + groupid + "','" + userid + "',(SELECT course_id FROM courses WHERE courses.name = '" + coursename + "'));";
            for (int i = 0; i < guserids.length; i++) {
                statement += "INSERT INTO groups(group_id,student_id,course_id) VALUES ('" + groupid + "','" + guserids[i] + "',(SELECT course_id FROM courses WHERE courses.name = '" + coursename + "'));";
            }
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement(statement);
            st.executeUpdate();
            st.close();
            con.disconnect();

        } catch (Exception e) {
            throw new SQLException("Unsuccessful group assignment.");
        }
    }
    //Assigns a student to a group
    public void single_project(String userid,String coursename) throws SQLException {
        try {
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("INSERT INTO groups(group_id,student_id,course_id) VALUES (?,?,(select course_id from courses where courses.name = ?));");
            st.setString(1, userid);
            st.setString(2, userid);
            st.setString(3, coursename);
            st.executeUpdate();
            st.close();
            con.disconnect();
        } catch (Exception e) {
            throw new SQLException("Unsuccessful group assignment.");
        }
    }
    //Returns true if students with specific IDs exist and are available for a group and false if one or more IDs don't exist or aren't available
    public ResultSet check_team(String userid, String coursename) throws  SQLException {
        try {
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("SELECT EXISTS (SELECT group_id FROM groups WHERE groups.student_id = ? AND groups.course_id IN " +
                    "(SELECT course_id FROM courses WHERE courses.name = ?));");
            st.setString(1, userid);
            st.setString(2,coursename);
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (Exception e) {
            throw new SQLException("Could not obtain data.");

        }
    }
    //Returns student id given a group and a course
    public ResultSet GetID(String group,String course) throws SQLException{
        try {
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("SELECT student_id FROM groups WHERE group_id=? AND course_id=?");
            st.setString(1,group);
            st.setString(2,course);
            ResultSet Rs = st.executeQuery();
            return Rs;
        }catch(Exception e){
            throw new SQLException("Could not get ID");
        }
    }
    //Deletes all groups from a course
    public void DeleteGroupsFromCourse(String courseid) throws SQLException {
        try {
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("DELETE FROM groups WHERE course_id=? ;");
            st.setString(1, courseid);
            st.executeUpdate();
            st.close();
            con.disconnect();
        } catch (Exception e) {
            throw new SQLException("Could not delete groups");
        }
    }
}