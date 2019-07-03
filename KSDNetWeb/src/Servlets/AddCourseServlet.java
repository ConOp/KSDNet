package Servlets;

import Classes.CourseMapper;

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
import javax.servlet.RequestDispatcher;


@WebServlet(name = "AddCourseServlet",value ="/Add_course")

public class AddCourseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        request.setCharacterEncoding("UTF-8"); //κωδικοποίηση χαρακτήρων request
        response.setCharacterEncoding("UTF-8");
        String userid = (String)request.getSession().getAttribute("username");
        String name = request.getParameter("name");
        String course_id = request.getParameter("course_id");
        int number_projects = Integer.parseInt(request.getParameter("number_projects"));

        try {

            CourseMapper cm = new CourseMapper();
            cm.createCourse(course_id,userid,name,number_projects);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TeacherHomepage");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            PrintWriter out = response.getWriter();	//για εκτύπωση στην html
            String title = "Course already exists";
            String docType ="<!doctype html public\">\n";
            out.println(docType +
                    "<html>\n" +
                    "<head><title>" + title + "</title><style>input[type=button]{margin:50px 42% auto;font-size:10pt;font-weight:bold;}" +
                    "</style></head>" +
                    "<body bgcolor = \"#f0f0f0\">" +
                    "<h1 align = \"center\">" + title + "</h1>" +
                    "<h3 align=\"center\">Courseid already used!!!</h3>"+
                    "<input onclick=\"location.href='new_course.html'\" type=\"button\" value=\"GO_BACK_TO_ADD_COURSE\">"+"</body></html>");
            return;
        }
    }
}
