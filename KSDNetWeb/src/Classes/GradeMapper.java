package Classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
}
