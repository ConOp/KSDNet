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

@WebServlet(name = "GradeServlet", value="/GradingTeacher")
public class GradeServlet extends HttpServlet {
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

        PrintWriter  out=response.getWriter();
        String id = request.getParameter("name");
        ResultSet Rs=null;


        out.println("<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Teacher Homepage</title>" +
                "<link href=\"./bootstrap/css/bootstrap.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-grid.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-reboot.css\" rel=\"stylesheet\"><link rel=\"stylesheet\" href=\"./bootstrap/css/bootstrap.css\">" +
                "</head><body><div class=\"container d-flex justify-content-center\">\n" +
                "<div class=\"shadow p-3 mb-5 bg-white rounded\">\n" +
                "<div class=\"card text-center \" style=\"width: 45rem;\"><div class=\"card-body\">\n" +
                "<h5 class=\"card-title\">Κατάλογος Ομάδων</h5>\n" +
                "<div class = \"col\">\n" +
                "<form method=\"post\" action=\"/TeacherHomepage\"><ul class=\"list-group list-group-flush\">");


        try{
            Connection con = ds.getConnection();
            PreparedStatement st = con.prepareStatement("SELECT  group_id,totalgrade FROM groups WHERE course_id='"+id+"'"); //παίρνουμε το userid από τη βάση
            Rs = st.executeQuery();
            PrintResults(Rs,out);
            if (request.getParameter("logout") != null) {
                request.getSession().removeAttribute("username");
                request.getSession().invalidate();
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.sendRedirect("/TeacherHomepage");
            }
            st.close();

        }catch(Exception e){

        }
    }
    protected void PrintResults(ResultSet rs,PrintWriter out) {
        try {
            while (rs.next()){
                out.println("<a href=\"#\" class=\"list-group-item list-group-item-action\">"+rs.getString("group_id")+"</a>");
            }
            out.println("</ul><br>" +
                    "<input type=\"button\" onclick=\"location.href='new_course.html'\" value=\"Add new course\"><br>" +
                    "<br><input type=\"submit\" id=\"log\" value=\"LOGOUT\" name=\"logout\">\n" +
                    "</form></div></div></div></div></div>" +
                    "<script src=\"./bootstrap/js/bootstrap.bundle.js\" ></script>" +
                    "<script src=\"./bootstrap/js/bootstrap.js\" ></script>" +
                    "</body>" +
                    "</html>");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
