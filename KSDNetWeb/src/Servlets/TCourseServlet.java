package Servlets;

import Classes.CourseMapper;
import Classes.Dbconnector;
import Classes.ProjectMapper;

import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@WebServlet(name = "TCourseServlet",value = "/TCourse")
public class TCourseServlet extends HttpServlet {
    public static String Coursename="";
    public static String Courseid="";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                " <body>" );
        try {
            Coursename = request.getParameter("coursename");
            Dbconnector con = new Dbconnector();
            CourseMapper cm = new CourseMapper();
            ResultSet Rs =cm.get_courseid(Coursename);
            Rs.next();
            Courseid= Rs.getString("course_id");
        }
        catch (Exception e){

        }
        try {
            out.println("<h1  name=\"coursename\" >" + Coursename + "</h1>" +
                    "<h3>Current Projects:</h3><br>");
            out.println(       "<form action=\"/TCourse\" method=\"Post\">" +
                    "    <input type=\"submit\" name=\"DeleteCourse\" value=\"Delete Course\" />" +
                    "</form>" +
                    "<form action=\"/GradingTeacher\" method=\"Post\">" +
                    "<input type=\"submit\" name=\""+Courseid+"\" value=\"Assign Grades\" />"+
                    "</form>" +
                    "<input type=\"submit\" name=\"CreateProject\" onclick=\"location.href='new_project.html'\" value=\"Create Project\"/>" +
                    "<table><tr><td><h3>Project ID</h3></td><td><h3>Group-Members</h3></td></tr>");

            Dbconnector con = new Dbconnector();
            ProjectMapper pm = new ProjectMapper();
            System.out.println(Courseid);
            ResultSet Rs = pm.get_projectid_groupmembers(Courseid);

            while(Rs.next()){
                out.println("<tr><td><h2>"+Rs.getString("project_id")+"</h2></td><td><h2>"+Rs.getString("groupmembers")+"</h2></td></tr>");
            }

            out.println("</table>");

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
        else if ( request.getParameter("added_project")!= null){

            String projectid = request.getParameter("ProjectID");
            String groupmembers = request.getParameter("groupmembers");
            String deadline =request.getParameter("Deadline");
            String max_grade = request.getParameter("maxgrade");
            int max = Integer.parseInt(max_grade);
            try {
                ProjectMapper pm = new ProjectMapper();
                pm.createProject(projectid, Courseid, groupmembers, deadline, max_grade);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TCourse?coursename="+Coursename);
                dispatcher.forward(request, response);
            }
            catch (Exception e){

            }

        }
    }
}

