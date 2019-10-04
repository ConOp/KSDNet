package Servlets;

import Classes.ProjectMapper;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "ProjectServ",value = "/CreateNewProject")
public class ProjectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectid = request.getParameter("ProjectID");//gets project id from form
        String course_id = (String)request.getSession().getAttribute("courseid");//gets course id from session
        String deadline =request.getParameter("Deadline");//gets deadline from form
        String max_grade = request.getParameter("maxgrade");//gets max grade from form
        try {
            //Creates a project
            ProjectMapper pm = new ProjectMapper();
            pm.createProject(projectid,course_id,deadline, max_grade);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TCourse");
            dispatcher.forward(request, response);
        }
        catch (Exception e){

            PrintWriter out = response.getWriter();	//Prints html
            String title = "Project already exists";
            String docType ="<!doctype html public\">\n";
            out.println(docType +
                    "<html>\n" +
                    "<head><title>" + title + "</title><style>input[type=button]{margin:50px 42% auto;font-size:10pt;font-weight:bold;}" +
                    "</style></head>" +
                    "<body bgcolor = \"#f0f0f0\">" +
                    "<h1 align = \"center\">" + title + "</h1>" +
                    "<h3 align=\"center\">Project id already used!!!</h3>"+
                    "<input onclick=\"location.href='new_project.html'\" type=\"button\" value=\"GO_BACK_TO_ADD_PROJECT\">"+"</body></html>");
            return;
        }

    }


}
