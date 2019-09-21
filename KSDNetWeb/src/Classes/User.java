package Classes;

import java.sql.SQLException;
//User interface for students and teachers
public interface User {
    public void register(String id,String name,String surname,String password,String email,byte[] salt) throws SQLException;
    public boolean login(String username,String password) throws SQLException;
}
