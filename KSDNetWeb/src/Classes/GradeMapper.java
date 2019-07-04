package Classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GradeMapper {
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
}
