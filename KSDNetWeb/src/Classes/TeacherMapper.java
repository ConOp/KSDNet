package Classes;

import Servlets.RegisterServlet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherMapper implements  User {
    public void register(String id,String name,String surname,String password,String email,byte[] salt) throws SQLException {
        try{
            Dbconnector con = new Dbconnector();
            PreparedStatement st = con.connect().prepareStatement("INSERT INTO teachers (teacher_id,name,surname,password,email,salt) VALUES(?,?,?,?,?,?);");
            st.setString(1, id);
            st.setString(2, name);
            st.setString(3, surname);
            st.setString(4, password);
            st.setString(5, email);
            st.setBytes(6, salt);
            st.executeUpdate();
            st.close();
            con.disconnect();
        }catch(Exception e){
            throw new SQLException("Teacher could not register");
        }
    }

    public boolean login(String username,String password) throws SQLException {
        try{
            Dbconnector con = new Dbconnector();
            PreparedStatement sm = con.connect().prepareStatement("SELECT teacher_id, password, salt FROM teachers where teacher_id = '"+ username +"';");
            ResultSet Rs1 = sm.executeQuery();
            if(Rs1.next()) {
                byte[] salt = Rs1.getBytes("salt");
                String securePassword = RegisterServlet.SecurePassword(password,salt); /*υπολογισμός του hashed&salted password με βάση τα στοιχεία του χρήστη(pass),
								 										και το salt της βάσης, αφού υπάρχει χρήστης με τέτοιο id*/
                if(username.equals(Rs1.getString("teacher_id"))&&securePassword.equals(Rs1.getString("password"))) { //έλεγχος έγκυρου password και username
                    con.disconnect();
                    return true;
                }
            }else{
                return false;
            }
        }catch(Exception e){
            System.out.println(e);
            throw new SQLException("Incorrect credentials");
        }
        return false;
    }
}
