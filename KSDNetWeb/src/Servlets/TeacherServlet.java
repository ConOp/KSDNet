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

@WebServlet(name = "TeacherServlet", value="/TeacherHomepage")
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

        String userid = (String)request.getSession().getAttribute("username");

        PrintWriter  out=response.getWriter();
        out.println("<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Teacher Homepage</title>" +
                "<link href=\"./bootstrap/css/bootstrap.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-grid.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-reboot.css\" rel=\"stylesheet\"><link rel=\"stylesheet\" href=\"./bootstrap/css/bootstrap.css\">" +
                "</head><body>" +
                "<div class=\"container d-flex justify-content-center\">\n" +
                "<div class=\"shadow p-3 mb-5 bg-white rounded\">\n" +
                "<div class=\"card text-center \" style=\"width: 45rem;\"><div class=\"card-body\">\n" +
                "<h5 class=\"card-title\">Course List</h5>\n" +
                "<h6 class=\"card-subtitle mb-2 text-muted\">Selected Courses</h6><div class = \"col\">\n");


        try{
            Connection con = ds.getConnection();
            PreparedStatement st = con.prepareStatement("SELECT name FROM courses WHERE teacher_id = '"+userid+"'"); //παίρνουμε το userid από τη βάση
            ResultSet Rs = st.executeQuery();
            PrintResults(Rs,out);
            if (request.getParameter("logout") != null) {

                request.getSession().removeAttribute("username");
                request.getSession().invalidate();
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.sendRedirect("index.html");
            }
            st.close();
            con.close();
        }catch(Exception e){

        }
    }
    protected void PrintResults(ResultSet rs,PrintWriter out) {
        try {
            int i =0;
            out.println("<form method=\"POST\" action=\"/TCourse\">" +
                    "<ul class=\"list-group list-group-flush\">");
            while (rs.next()){
                out.println("<input type=\"submit\" name=\"coursename\" id="+i+" class=\"list-group-item list-group-item-action\" value=\""+rs.getString("name")+"\">");
            i++;
            }
            out.println("</form>");
            out.println("</ul><br>" +
                    "<form method=\"post\" action=\"/TeacherHomepage\">" +
                    "<input type=\"button\" onclick=\"location.href='new_course.html'\"  value=\"Add new course\"><br>" +
                    "<input type=\"submit\" id=\"log\"  value=\"LOGOUT\" name=\"logout\">\n" +
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
