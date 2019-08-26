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

        out.println("<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>"+Coursename+"</title>" +
                "<link href=\"./bootstrap/css/bootstrap.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-grid.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-reboot.css\" rel=\"stylesheet\">" +
                "</head><body><div class=\"container d-flex justify-content-center\">" +
                "<div class=\"shadow p-3 mb-5 bg-white rounded\">" +
                "<div class=\"card text-center \" style=\"width: 45rem;\">" +
                "<div class=\"card-body\"><div>" +
                "<div style=\"float: left;\">" +
                "<form action=\"/TeacherHomepage\" method=\"Post\">" +
                "<input name=\"backbutton\" type=\"submit\" value=\"Go Back\"></form></div>" +
                "<div style=\"float: right;\"><form float=\"left\" action=\"/TCourse\" method=\"post\">" +
                "<input type=\"submit\" name=\"DeleteCourse\" value=\"Delete Course\">" +
                "</form></div></div><br><h5 name=\"coursename\" class=\"card-title\">"+Coursename+"</h5><br>" +
                "<h6 class=\"card-subtitle mb-2 text-muted\">Course Information</h6>" +
                "<div class = \"col\"><br><table align=\"center\" class=\"table table-bordered\">" +
                "<tr><th>Project ID</th><th>Deadline</th><th>Members of group</th></tr>");

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
        int numAllowed = 0;
        try {
            ProjectMapper pm = new ProjectMapper();
            ResultSet Rs = pm.project_info(Courseid);

            try{
                CourseMapper cm = new CourseMapper();
                numAllowed = cm.getnumofprojects(Courseid);
            }catch (Exception e){}
            int numExisting = 0;


            while(Rs.next()){
                out.println("<tr><td>" + Rs.getString("project_id") + "</td><td>" + Rs.getString("deadline") + "</td><td>" + Rs.getInt("groupmembers") + "</td></tr>");
                numExisting+=1;
            }
            out.println("</table><br>" +
                    "<form class=\"justify-content-center\" action=\"/GradingTeacher\" method=\"post\">" +
                                "<div class=\"form-group\"><label style=\"padding-right:5px;\">Grade assign of the project:</label>");
            Rs.beforeFirst();
            while (Rs.next()){
                if(pm.DeadlineHasPassed(Rs.getString("project_id"))){
                    activeProject=false;
                    out.println("<input id=\""+Rs.getString("project_id")+"\" title=\" \" type=\"submit\" name =\"ProjectID\" value=\""+Rs.getString("project_id")+"\">");

                }else{
                    activeProject=true;
                    out.println("<input id=\""+Rs.getString("project_id")+"\" title=\" \" type=\"submit\" name =\"ProjectID\" value=\""+Rs.getString("project_id")+"\" disabled>");
                }
                out.println("<script>var grade_assign = document.getElementById('"+Rs.getString("project_id")+"\').disabled;\n" +
                        "if(grade_assign){document.getElementById('"+Rs.getString("project_id")+"\').title = \"Grading will be available past the deadline\";}" +
                        "else{document.getElementById('"+Rs.getString("project_id")+"\').title = \"Grade assignment available\";}</script>");

            }

            out.println("</div></form><br>\n" +
                    "<form action=\"/DownloadProject\" method=\"Post\">\n" +
                    "<input type=\"submit\" name=\"DownloadProject\" value=\"Download Project\">\n" +
                    "</form><br>");

            if(activeProject || numAllowed<=numExisting){
                out.println("<input disabled type=\"submit\" name=\"CreateProject\" onclick=\"location.href='new_project.html'\" value=\"Create Project\" name=\"createproject\" id=\"createproject\"/>");
            }else{
                out.println("<input type=\"submit\" name=\"CreateProject\" onclick=\"location.href='new_project.html'\" value=\"Create Project\" name=\"createproject\" id=\"createproject\"/>");
            }

            out.print("</div></div></div></div></div>" +
                    "<script src=\"./bootstrap/js/bootstrap.bundle.js\"></script><script src=\"./bootstrap/js/bootstrap.js\"></script>" +
                    "</body> </html>");


        } catch (Exception e) {
            System.out.println(e);
        }

    }
}

