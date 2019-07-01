package Classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectMapper {
    public void createProject(String projectid,String courseid,String groupmembers,String deadline,String maxgrade) throws SQLException {
        try {
            int max = Integer.parseInt(maxgrade);
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date parsed = df1.parse(deadline);
            Timestamp tm = new Timestamp(parsed.getTime());
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("INSERT INTO projects(project_id, course_id,groupmembers, deadline,max_grade) VALUES (?,?,?,?,?)");
            st.setString(1, projectid);
            st.setString(2, courseid);
            st.setString(3, groupmembers);
            st.setTimestamp(4, tm);
            st.setInt(5, max);
            st.executeUpdate();
            st.close();
            connector.disconnect();
        } catch (Exception e) {
            throw new SQLException("Could not create  project.");
        }
    }

        public void deleteProject(String projectid) throws SQLException {
            try {
                Dbconnector connector = new Dbconnector();
                PreparedStatement st = connector.connect().prepareStatement("DELETE FROM projects WHERE  project_id=?");
                st.setString(1, projectid);
                st.executeUpdate();
                st.close();
                connector.disconnect();
            } catch (Exception e) {
                throw new SQLException("Could not delete  project.");
            }
        }
        public void deleteallProjects(String courseid) throws SQLException {
            try {
                Dbconnector connector = new Dbconnector();
                PreparedStatement st = connector.connect().prepareStatement("DELETE FROM projects WHERE  course_id=?");
                st.setString(1, courseid);
                st.executeUpdate();
                st.close();
                connector.disconnect();
            } catch (Exception e) {
               throw new SQLException("Could not delete  projects.");
         }
    }
    public ResultSet get_projectid_groupmembers(String courseid) throws  SQLException{
        try{
            Dbconnector connector = new Dbconnector();
            PreparedStatement st = connector.connect().prepareStatement("SELECT project_id,groupmembers FROM projects WHERE  course_id=?");
            st.setString(1, courseid);
            ResultSet rs = st.executeQuery();
            return  rs;
        }
        catch (Exception e){
            throw new SQLException("Could not obtain data.");

        }

    }
}
