package Servlets;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "TeacherServlet", value="/Teacher")
public class TeacherServlet extends HttpServlet {
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
        response.setContentType("text/html; charset=UTF-8"); //θέτει τον τύπο περιεχομένου της απάντησης που αποστέλλεται στον πελάτη, εάν η απάντηση δεν έχει δεσμευτεί ακόμα
        request.setCharacterEncoding("UTF-8"); //κωδικοποίηση χαρακτήρων request
        response.setCharacterEncoding("UTF-8");



        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        try{
            Connection con = ds.getConnection();
            PreparedStatement st = con.prepareStatement("Select name From courses"); //παίρνουμε το userid από τη βάση
            ResultSet Rs = st.executeQuery();
            while(Rs.next()){
                out.println("<h1>\""+Rs.getString(0)+"\"</h1><br>");//doesn't print for some reason...
            }
            out.println("<h1>\"Nothingness\"</h1>");//doesn't print either...
        }catch(Exception e){}
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
