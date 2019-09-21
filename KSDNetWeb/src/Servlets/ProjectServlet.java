package Servlets;

import Classes.ProjectMapper;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;


@WebServlet(name = "ProjectServ",value = "/CreateNewProject")
public class ProjectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectid = request.getParameter("ProjectID");//gets project id from form
        String course_id = (String)request.getSession().getAttribute("courseid");//gets course id from session
        String deadline =request.getParameter("Deadline");//gets deadline from form
        String max_grade = request.getParameter("maxgrade");//gets max grade from form
        try {
            ProjectMapper pm = new ProjectMapper();
            pm.createProject(projectid,course_id,deadline, max_grade);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TCourse");
            dispatcher.forward(request, response);
        }
        catch (Exception e){

        }

    }


}
