package Servlets;

import Classes.CourseMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;


@WebServlet(name = "AddCourseServlet",value ="/Add_course")

public class AddCourseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        request.setCharacterEncoding("UTF-8"); //Request character encoding
        response.setCharacterEncoding("UTF-8"); //Responses character encoding
        String userid = (String)request.getSession().getAttribute("username");//gets user id from session
        String name = request.getParameter("name");//gets user name from form
        String course_id = request.getParameter("course_id");//gets course id from form
        int groupmbembers= Integer.parseInt(request.getParameter("groupmembers"));//gets groupmembers from form
        int number_projects = Integer.parseInt(request.getParameter("number_projects"));//gets number of projects from form

        try {
            //Create a course
            CourseMapper cm = new CourseMapper();
            cm.createCourse(course_id,userid,name,number_projects,groupmbembers);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TeacherHomepage");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            PrintWriter out = response.getWriter();	//Prints html
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
