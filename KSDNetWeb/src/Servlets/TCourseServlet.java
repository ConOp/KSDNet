package Servlets;

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
    private DataSource ds = null;
    public static String Coursename="";
    public static String Courseid="";
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
        out.println("<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                " <body>" );
        if (request.getParameter("DeleteCourse") != null) {
            try {
                Connection con = ds.getConnection();
                System.out.println("SELECT * FROM courses WHERE courses.name = '" + request.getParameter("coursename") + "'");

                PreparedStatement st = con.prepareStatement("DELETE FROM  courses WHERE name='"+Coursename+"'");
                int val = st.executeUpdate();
                st = con.prepareStatement("DELETE FROM  projects WHERE course_id='"+Courseid+"'");
                 val = st.executeUpdate();
                st.close();
                con.close();
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TeacherHomepage");
                dispatcher.forward(request, response);
            }
         catch (Exception e) {

        }
        }
        else if (request.getParameter("coursename") != null){
            Coursename=request.getParameter("coursename");
            try {
                Connection con = ds.getConnection();
                System.out.println("SELECT * FROM courses WHERE courses.name = '" + request.getParameter("coursename") + "'");
                out.println("<h1  name=\"coursename\" >" + Coursename + "</h1>" +
                        "<h3>Current Projects:</h3><br>");
                out.println(       "<form action=\"/TCourse\" method=\"Post\">" +
                        "    <input type=\"submit\" name=\"DeleteCourse\" value=\"Delete Course\" />" +
                        "</form>" +
                                "<form action=\"/GradingTeacher\" method=\"Post\">" +
                                "<input type=\"submit\" name=\""+Courseid+"\" value=\"Assign Grades\" />"+
                        "</form>" +
                        "<input type=\"submit\" name=\"CreateProject\" onclick=\"location.href='new_project.html'\" value=\"Create Project\" />");

                PreparedStatement st = con.prepareStatement("SELECT course_id FROM courses WHERE name = '"+Coursename+"'");
                ResultSet Rs = st.executeQuery();
                Rs.next();
               Courseid= Rs.getString("course_id");

                 st = con.prepareStatement("SELECT project_id,groupmembers FROM projects WHERE course_id = '"+Courseid+"'");
                 Rs = st.executeQuery();

                    while(Rs.next()){
                        out.println("<a>"+Rs.getString("project_id")+","+Rs.getString("groupmembers")+"</a>");
                    }



                st.close();
            } catch (Exception e) {

            }
        }
        else if ( request.getParameter("added_project")!= null){
            String projectid = request.getParameter("ProjectID");
            String groupmembers = request.getParameter("groupmembers");
            String deadline =request.getParameter("Deadline");
            String max_grade = request.getParameter("maxgrade");
            int max = Integer.parseInt(max_grade);
            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            //OffsetDateTime odt = OffsetDateTime.parse( deadline );
            try {

                Date parsed = df1.parse(deadline);
                Connection con = ds.getConnection();

                PreparedStatement st = con.prepareStatement("INSERT INTO projects(project_id, course_id,groupMembers, deadline,max_grade) VALUES ('"+projectid+"','"+Courseid+"','"+groupmembers+"','"+parsed+"',"+max+")");
               int val = st.executeUpdate();

            }
            catch (Exception e){
                System.out.println("CANT GET DATE");
            }

        }
    }
}

