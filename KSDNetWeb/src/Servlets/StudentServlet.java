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
import java.sql.SQLException;

@WebServlet(name = "StudentServlet", value="/StudentHomepage")
public class StudentServlet extends HttpServlet {
    private DataSource ds = null;
    public void init() throws ServletException { //φορτώνεται ο servlet και καλείται η init, για αρχικοποιήσεις και σύνδεση με τη βάση
        try {
            InitialContext ctx = new InitialContext(); //πόροι για datasource
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/postgres"); //lookup δεσμεύει το αντικείμενο ds τύπου datasource με το string που θέλουμε
        } catch (Exception e) {
            throw new ServletException(e.toString());
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8"); //θέτει τον τύπο περιεχομένου της απάντησης που αποστέλλεται στον πελάτη, εάν η απάντηση δεν έχει δεσμευτεί ακόμα
        request.setCharacterEncoding("UTF-8"); //κωδικοποίηση χαρακτήρων request
        response.setCharacterEncoding("UTF-8");
        PrintWriter  out=response.getWriter();
        out.println("<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "</head>" +
                "<body>");
        try {
            Connection con = ds.getConnection();
            PreparedStatement st = con.prepareStatement("SELECT * FROM COURSES");
            ResultSet rs= st.executeQuery();
            PrintResults(rs,out);

        }
        catch (Exception e){

        }

    }

    protected void PrintResults(ResultSet rs,PrintWriter out) {
        out.println("<table><tr><td>Course ID</td><td>Course Name</td><td>Number Of Projects</td></tr>");
        try {
            while (rs.next()){
                out.println("<tr><td>"+rs.getString("course_id")+"</td><td>"+rs.getString("name")+"</td><td>"+rs.getString("number_projects")+"</td></tr>");
            }
            out.println("</table>");
        }
        catch (SQLException e){

        }
    }
}
