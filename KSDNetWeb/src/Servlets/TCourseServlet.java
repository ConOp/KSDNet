package Servlets;

import Classes.CourseMapper;
import Classes.ProjectMapper;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;


@WebServlet(name = "TCourseServlet",value = "/TCourse")
public class TCourseServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Coursename="";
        String Courseid="";
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                " <body>" );
        if (request.getParameter("coursename") != null) {
                Coursename = request.getParameter("coursename");
                request.getSession().setAttribute("coursename",Coursename);
            try {
                CourseMapper cm = new CourseMapper();
                Courseid =cm.get_courseid(Coursename);
                request.getSession().setAttribute("courseid",Courseid);
            }
            catch (Exception e){

            }
        }
        else{
            Coursename=(String)request.getSession().getAttribute("coursename");
            Courseid=(String)request.getSession().getAttribute("courseid");
        }
        try {
            out.println("<h1  name=\"coursename\" >" + Coursename + "</h1>" +
                    "<h3>Current Projects:</h3><br>");
            out.println("<form action=\"/TCourse\" method=\"Post\">" +
                    "    <input type=\"submit\" name=\"DeleteCourse\" value=\"Delete Course\" />" +
                    "</form>" +
                    "<input type=\"submit\" name=\"CreateProject\" onclick=\"location.href='new_project.html'\" value=\"Create Project\"/>" +
                    "<form action=\"/GradingTeacher\" method=\"Post\">" +
                    "<table><tr><td><h3>Project ID</h3></td><td><h3>Group-Members</h3></td></tr>");


            ProjectMapper pm = new ProjectMapper();
            ResultSet Rs = pm.get_projectid(Courseid);

            while(Rs.next()){
                if(pm.DeadlineHasPast(Rs.getString("project_id"))){
                    out.println("<tr><td><p>Grading will be available past the deadline</p><input type=\"submit\" name =\"ProjectID\"value=\""+Rs.getString("project_id")+"\" disabled></h2></td></tr>");
                }else{
                    out.println("<tr><td><input type=\"submit\" name =\"ProjectID\"value=\""+Rs.getString("project_id")+"\"</h2></td></tr>");
                }
            }

            out.println("</table></form>");
            out.println("<form action=\"/DownloadProject\" method=\"Post\">" +
                    "<input type=\"submit\" name=\"DownloadProject\" value=\"Download Project\" />");

        } catch (Exception e) {

        }

        if (request.getParameter("DeleteCourse") != null) {
            try {
                CourseMapper cm = new CourseMapper();
                cm.deleteCourse(Courseid);
                ProjectMapper pm = new ProjectMapper();
                pm.deleteallProjects(Courseid);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TeacherHomepage");
                dispatcher.forward(request, response);
            }
         catch (Exception e) {

        }
        }

    }
}

