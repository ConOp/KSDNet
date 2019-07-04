package Servlets;

import Classes.ProjectMapper;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "ProjectServ",value = "/CreateNewProject")
public class ProjectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectid = request.getParameter("ProjectID");
        String course_id = (String)request.getSession().getAttribute("courseid");
        String deadline =request.getParameter("Deadline");
        String max_grade = request.getParameter("maxgrade");
        int max = Integer.parseInt(max_grade);
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
