package Servlets;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import javax.servlet.http.HttpSession;
import javax.sql.*;
import java.security.*;

@WebServlet(name ="UserServlet",value ="/Login")
public class UserServlet extends HttpServlet {

    static String generatedPassword = null;
    String securePassword = null;
    static byte[] salt;

    private static final long serialVersionUID = 1L;
    private DataSource ds = null;
    public void init() throws ServletException { //φορτώνεται ο servlet και καλείται η init, για αρχικοποιήσεις και σύνδεση με τη βάση
        try {
            InitialContext ctx = new InitialContext(); //πόροι για datasource
            ds = (DataSource)ctx.lookup("java:comp/env/jdbc/postgres"); //lookup δεσμεύει το αντικείμενο ds τύπου datasource με το string που θέλουμε
        }catch(Exception e) {
            throw new ServletException(e.toString());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        request.setCharacterEncoding("UTF-8"); //κωδικοποίηση χαρακτήρων request
        response.setCharacterEncoding("UTF-8");

        if(request.getParameter("registerButton")!=null) { //έλεγχος αν πατήθηκε το sign up
            String uni_type = request.getParameter("option"); //S or T
            String uname = request.getParameter("uname");
            String surname = request.getParameter("surname");
            String userid = request.getParameter("userid");
            String pass = request.getParameter("pass");
            String email = request.getParameter("email");
            System.out.println("hello");
            response.sendRedirect("index.html");

            try {
            /*    byte[] salt2 = getSalt();
                securePassword = SecurePassword(pass, salt2);
                //Connection con = ds.getConnection();
                PreparedStatement st = con.prepareStatement("INSERT INTO students(username,password,salt,department) VALUES(?,?,?,?)");
                st.setInt(1, uname2);
                st.setString(2, securePassword);
                st.setBytes(3, salt2);
                st.setString(4, dept);
                st.executeUpdate();*/

            } catch (Exception e) {

            }

        }
    }
}
