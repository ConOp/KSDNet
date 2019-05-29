package Servlets;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.http.HttpSession;
import javax.sql.*;
import java.security.*;

@WebServlet(name ="Servlets.UserServlet",value ="/Register")
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

        String uni_type = request.getParameter("option"); //S or T
        String uname = request.getParameter("uname");
        String surname = request.getParameter("surname");
        String userid = request.getParameter("userid");
        String pass = request.getParameter("pass");
        String email = request.getParameter("email");

        try {
            salt = getSalt();
            securePassword = SecurePassword(pass, salt);
            Connection con = ds.getConnection();
            PreparedStatement st = con.prepareStatement("INSERT INTO students(student_id,name,surname,password,email,salt) VALUES(?,?,?,?,?,?);");
            st.setString(1, userid);
            st.setString(2, uname);
            st.setString(3, surname);
            st.setString(4, securePassword);
            st.setString(5, email);
            st.setBytes(6, salt);
            st.executeUpdate();
            st.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        PrintWriter out = response.getWriter();	//για εκτύπωση στην html
        String title = "Registration";
        String docType ="<!doctype html public\">\n";
        out.println(docType +
           "<html>\n" +
           "<head><title>" + title + "</title><style>input[type=button]{margin:50px 42% auto;font-size:10pt;font-weight:bold;}" +
           "</style></head>\n" +
           "<body bgcolor = \"#f0f0f0\">\n" +
           "<h1 align = \"center\">" + title + "</h1>\n" +
           "<h3 align=\"center\">Successfull registration!!!</h3>"+
           "<input onclick=\"location.href='index.html'\" type=\"button\" value=\"GO_BACK_TO_LOGIN\">"+"</body></html>");


    }

    private static String SecurePassword(String pass,byte[] salt) {

        try {
            //αρχικοποιήση αντικειμένου MessageDigest χρησιμοποιώντας τον αλγόριθμο SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //update του αντικείμενου, χρησιμοποιώντας το salt
            md.update(salt);
            //τελική ενημέρωση του md χρησιμοποιώντας τα bytes του password που έδωσε ο χρήστης σαν input
            byte[] hash = md.digest(pass.getBytes());
            //μετατροπή των bytes από decimal format σε hexadecimal format
            StringBuffer sb = new StringBuffer();
            for(int i=0; i< hash.length ;i++)
            {
                sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            //hex format
            generatedPassword = sb.toString(); //τελικό hashed&salted password
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;

    }
    //Προσθήκη salt, αυτή η μέθοδος χρηαιμοποιήθηκε για τη δημιούργια salt, και με ένα update μέσω prepared statement, έγινε η προσθήκη του salt στη βάση
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        //δημιουργία ενός 8 byte (64 bit) salt array
        byte[] salt2 = new byte[8];
        //παίρνουμε ένα random salt και γεμίζουμε τον πίνακα
        sr.nextBytes(salt2);
        return salt2;

    }
}
