package Servlets;

import javax.jms.Session;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "St_CourseServlet",value = "/Student_CourseHomepage")
public class St_CourseServlet extends HttpServlet {

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

        PrintWriter out = response.getWriter();
        String userid = (String) request.getSession().getAttribute("username");
        String coursename = "";
        if(userid.charAt(0) != 'S') {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        else {
            coursename = request.getParameter("coursename");
            request.getSession().setAttribute("coursename",coursename);

            out.println("<!DOCTYPE HTML>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<title>Course Details</title>" +
                    "<link href=\"./bootstrap/css/bootstrap.css\" rel=\"stylesheet\">" +
                    "<link href=\"./bootstrap/css/bootstrap-grid.css\" rel=\"stylesheet\">" +
                    "<link href=\"./bootstrap/css/bootstrap-reboot.css\" rel=\"stylesheet\"><link rel=\"stylesheet\" href=\"./bootstrap/css/bootstrap.css\">" +
                    "</head><body><div class=\"container d-flex justify-content-center\">" +
                    "<div class=\"shadow p-3 mb-5 bg-white rounded\">" +
                    "<div class=\"card text-center \" style=\"width: 45rem;\"><div class=\"card-body\">" +
                    "<h5 class=\"card-title\">" + coursename + "</h5><br>" +
                    "<h6 class=\"card-subtitle mb-2 text-muted\">Course Information</h6><div class = \"col\">" +
                    "<form method=\"post\" action=\"\" enctype=\"multipart/form-data\"><br>");

            try {
                Connection con = ds.getConnection();
                PreparedStatement st = con.prepareStatement("SELECT course_id, courses.name, teachers.surname, number_projects FROM COURSES INNER JOIN teachers on courses.teacher_id = teachers.teacher_id where courses.name='" + coursename + "';");
                ResultSet rs = st.executeQuery();

                out.println("<table align=\"center\" class=\"table table-bordered\">" + "<tr><th>" + "Course ID" + "</th><th>" + "Coursename" + "</th><th>" + "Teacher" + "</th><th>" + "Number of projects" + "</th></tr>");

                while (rs.next()) {
                    out.println("<tr><td>" + rs.getString("course_id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("surname") + "</td><td>" + rs.getString("number_projects") + "</td></tr>");
                }
                out.println("</table>" +
                        "<br><div class=\"input-group mb-3\">" +
                        "<div class=\"input-group-prepend\">" +
                        "<span class=\"input-group-text\" id=\"inputGroupFileAddon01\">Upload Project</span>" +
                        "</div><div class=\"custom-file\">" +
                        "<input name=\"file\" type=\"file\" class=\"custom-file-input\" id=\"inputGroupFile01\" aria-describedby=\"inputGroupFileAddon01\">" +
                        "<label class=\"custom-file-label\" for=\"inputGroupFile01\">Choose file</label></div>" +
                        "</div></form><br><form method=\"post\" action=\"/CreatGroup\"><input type=\"submit\" value=\"CREATE GROUP\" name=\"group\"></form></div></div></div></div></div>" +
                        "<script src=\"./bootstrap/js/bootstrap.bundle.js\"></script>" +
                        "<script src=\"./bootstrap/js/bootstrap.js\"></script></body></html>");
                st.close();
                rs.close();
                con.close();
                //Part file = request.getPart("file");
                //PreparedStatement st3 = con.prepareStatement("");
                //preparedStatement.setBinaryStream(3, logo);

            } catch (Exception e) {
            }

        }

    }

}