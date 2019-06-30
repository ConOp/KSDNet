package Classes;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Dbconnector {
    Connection con;
    public Connection connect() throws SQLException {
        try {
            InitialContext ctx = new InitialContext(); //πόροι για datasource
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/postgres"); //lookup δεσμεύει το αντικείμενο ds τύπου datasource με το string που θέλουμε
            con = ds.getConnection();
            return con;
        } catch (Exception e) {
            throw new SQLException("Can't connect to database");
        }
    }
    public void disconnect() throws Exception{
            try {
                con.close();
            }catch(Exception e) {
                throw new Exception("No existing connection to database");
            }
    }
}
