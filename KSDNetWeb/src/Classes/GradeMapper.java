package Classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class GradeMapper {
    //Inserts a grade to a specific group given the course
    public void insertGrade(String groupid,String projectid,int grade) throws SQLException{
        try{
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("UPDATE grade set grade=? WHERE  group_id=? and  project_id=?");
            st.setInt(1, grade);
            st.setString(2, groupid);
            st.setString(3, projectid);
            st.executeUpdate();
            st.close();
            connector.disconnect();
        }
        catch (Exception e){
            throw new SQLException("Could not insert grade.");
        }
    }
    //Returns the sum of grades of a student
    public int SumofGrades(String groupid) throws SQLException {
        try {
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("select grade from grade inner join projects on projects.project_id = grade.project_id where grade.group_id=?");
            st.setString(1, groupid);
            ResultSet rs = st.executeQuery();
            int sum = 0;
            while (rs.next()) {
                sum += rs.getInt(1);
            }
            connector.disconnect();
            return sum;
        } catch (Exception e) {
            throw new SQLException("Could not get sum.");
        }
    }
    //Uploads a file to database
    public void Uploadfile(String userid,String coursename,String projectid, Timestamp time,String filename,java.io.InputStream filecontent) throws SQLException{
        try{
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("INSERT INTO grade (group_id, project_id, upload_date, filename, file) VALUES((SELECT group_id FROM groups INNER JOIN courses on groups.course_id = courses.course_id WHERE groups.student_id = ? AND courses.name = ?),?,?,?,?)");
            st.setString(1, userid);
            st.setString(2, coursename);
            st.setString(3, projectid);
            st.setTimestamp(4, time);
            st.setString(5,filename);
            st.setBinaryStream(6,filecontent);
            st.executeUpdate();
            st.close();
            connector.disconnect();
        }
        catch (Exception e){
            throw new SQLException("Could not upload file.");
        }
    }
    //Updates a file in the database
    public void Updatefile(String filename,java.io.InputStream filecontent,String groupid) throws SQLException{
        try{
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("UPDATE grade SET filename = ?, file = ? WHERE grade.group_id=?");
            st.setString(1, filename);
            st.setBinaryStream(2, filecontent);
            st.setString(3,groupid);
            st.executeUpdate();
            st.close();
            connector.disconnect();
        }
        catch (Exception e){
            throw new SQLException("Could not update file.");
        }
    }
    //Returns all the files from all the groups of a specific project
    public ResultSet DownloadProject(String projectid) throws SQLException{
        try {
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("select filename,file from grade  inner join projects on grade.project_id= projects.project_id where grade.project_id=? and grade.grade is NULL ");
            st.setString(1, projectid);
            ResultSet rs = st.executeQuery();
            return rs;
        }catch (Exception e) {
            throw new SQLException("Could not download files.");
        }

    }
    //Returns all groups given a projectid without a grade
    public ResultSet GetGroup(String projectid) throws SQLException{
        try{
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("SELECT  group_id FROM grade WHERE project_id=? and grade is null ");
            st.setString(1,projectid);
            return st.executeQuery();
        }catch(Exception e){
            throw new SQLException("Could not get groups.");
        }
    }
    //Deletes all grades from all projects of a specific course
    public void DeleteGrades(String courseid) throws SQLException{
        try{
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("select grade.project_id from grade,projects where grade.project_id=projects.project_id and projects.course_id=?");
            st.setString(1, courseid);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                 st = con.connect().prepareStatement("DELETE FROM grade WHERE project_id=?");
                st.setString(1,rs.getString(1));
                st.executeUpdate();
            }
            st.close();
            con.disconnect();
        }catch(Exception e){
            throw new SQLException("Could not delete grades.");
        }
    }
    //Returns true if there is a projects which needs to be checked for grade and false if there isn't
    public Boolean project_exist_for_grade(String projectid){
        try{
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("SELECT * FROM grade WHERE project_id=? and grade is NULL");
            st.setString(1, projectid);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
        }
        return false;

    }
}
