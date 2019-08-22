package Servlets;

import Classes.CourseMapper;
import Classes.GradeMapper;
import Classes.GroupMapper;
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
        boolean activeProject = false;
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
                    "    <input type=\"submit\" name=\"DeleteCourse\" value=\"Delete Course\" /></form>");
             out.println("<form action=\"/GradingTeacher\" method=\"Post\">" +
                    "<table><tr><td><h3>Project ID</h3></td><td><h3>Group-Members</h3></td></tr>");
            ProjectMapper pm = new ProjectMapper();
            ResultSet Rs = pm.get_projectid(Courseid);

            while(Rs.next()){
                if(pm.DeadlineHasPassed(Rs.getString("project_id"))){
                    out.println("<tr><td><input type=\"submit\" name =\"ProjectID\"value=\""+Rs.getString("project_id")+"\"</h2></td></tr>");
                }else{
                    activeProject=true;
                    out.println("<tr><td><p>Grading will be available past the deadline</p><input type=\"submit\" name =\"ProjectID\"value=\""+Rs.getString("project_id")+"\" disabled></h2></td></tr>");
                }
            }
            out.println("</table></form>");
            out.println("<form action=\"/DownloadProject\" method=\"Post\">" +
                    "<input type=\"submit\" name=\"DownloadProject\" value=\"Download Project\" /></form>");

        } catch (Exception e) {

        }

        if(activeProject){
            out.println("<input disabled type=\"submit\" name=\"CreateProject\" onclick=\"location.href='new_project.html'\" value=\"Create Project\" name=\"createproject\" id=\"createproject\"/>");
        }else{
            out.println("<input type=\"submit\" name=\"CreateProject\" onclick=\"location.href='new_project.html'\" value=\"Create Project\" name=\"createproject\" id=\"createproject\"/>");
        }

        if (request.getParameter("DeleteCourse") != null) {
            try {
                GroupMapper gm = new GroupMapper();
                gm.DeleteGroupsFromCourse(Courseid);
                GradeMapper grm=new GradeMapper();
                grm.DeleteGrades(Courseid);
                ProjectMapper pm = new ProjectMapper();
                pm.deleteallProjects(Courseid);
                CourseMapper cm = new CourseMapper();
                cm.deleteCourse(Courseid);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TeacherHomepage");
                dispatcher.forward(request, response);
            }catch (Exception e) {

            }
        }

    }
}

