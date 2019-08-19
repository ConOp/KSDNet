package Servlets;

import Classes.CourseMapper;
import Classes.GroupMapper;
import Classes.ProjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "St_CourseServlet",value = "/Student_CourseHomepage")

public class St_CourseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean group_created = false;
        boolean project_exists = false;
        boolean send = false;
        PrintWriter out = response.getWriter();
        String userid = (String) request.getSession().getAttribute("username");
        String coursename = request.getParameter("coursename");
        String project_id = "";
        if(userid.charAt(0) != 'S') {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        else {
            request.getSession().setAttribute("coursename",coursename);

            out.println("<!DOCTYPE HTML>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<title>Course Details</title>" +
                    "<link href=\"./bootstrap/css/bootstrap.css\" rel=\"stylesheet\">" +
                    "<link href=\"./bootstrap/css/bootstrap-grid.css\" rel=\"stylesheet\">" +
                    "<link href=\"./bootstrap/css/bootstrap-reboot.css\" rel=\"stylesheet\">" +
                    "</head><body><div class=\"container d-flex justify-content-center\">" +
                    "<div class=\"shadow p-3 mb-5 bg-white rounded\">" +
                    "<div class=\"card text-center \" style=\"width: 45rem;\"><div class=\"card-body\">" +
                    "<h5 class=\"card-title\">" + coursename + "</h5><br>" +
                    "<h6 class=\"card-subtitle mb-2 text-muted\">Course Information</h6><div class = \"col\">" +
                    "<form method=\"post\" action=\"/UploadProject\" enctype=\"multipart/form-data\"><br>");

            try {
                GroupMapper gm = new GroupMapper();
                ResultSet r = gm.check_team(userid,coursename);
                while (r.next()){ group_created = r.getBoolean("exists");}
                r.close();

                CourseMapper c = new CourseMapper();
                ResultSet rs = c.course_info(coursename);

                out.println("<table align=\"center\" class=\"table table-bordered\">" + "<tr><th>" + "Course ID" + "</th><th>" + "Coursename" + "</th><th>" + "Teacher" + "</th><th>" + "Number of projects" + "</th><th>" + "Members of group" + "</th></tr>");

                while (rs.next()) {
                    out.println("<tr><td>" + rs.getString("course_id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("surname") + "</td><td>" + rs.getInt("number_projects") + "</td><td>" + rs.getInt("groupmembers") + "</td></tr>");
                }
                out.println("</table>");
                rs.close();

                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                ProjectMapper pm = new ProjectMapper();
                ResultSet details = pm.project_details(coursename,dateFormat.format(date));

                out.println("<table align=\"center\" class=\"table table-bordered\">" + "<tr><th>" + "Project ID" + "</th><th>" + "Deadline" + "</th><th>" + "Max grade" + "</th></tr>");
                while (details.next()){
                    out.println("<tr><td>" + details.getString("project_id") + "</td><td>" + details.getString("deadline") + "</td><td>" + details.getInt("max_grade") + "</td></tr>");
                    project_exists = true;
                    project_id += details.getString("project_id");
                }
                details.close();
                CourseMapper cm = new CourseMapper();
                String courseid= cm.get_courseid(coursename);
                GroupMapper groupmap= new GroupMapper();
                String grade=groupmap.getCourseGrade(userid,courseid);
                if(project_exists && group_created){
                    send = true;
                }
                out.println("</table><br><h5>Final grade "+grade+"/10</h5><br><div class=\"input-group mb-3\">" +
                        "<div class=\"input-group-prepend\">" +
                        "<span class=\"input-group-text\" id=\"inputGroupFileAddon01\">Project file</span>" +
                        "</div><div class=\"custom-file\">" +
                        "<input name=\"zipfile\" type=\"file\" accept=\".zip,.rar,.7zip\" class=\"custom-file-input\" id=\"inputGroupFile01\" aria-describedby=\"inputGroupFileAddon01\" required>" +
                        "<label id=\"upload_label\" class=\"custom-file-label\" for=\"inputGroupFile01\">Choose file</label></div>" +
                        "</div><br><input type=\"submit\" value=\"UPLOAD PROJECT\" name=\"upload\" id=\"upload_file\"></form><br><form method=\"post\" action=\"/CreatGroup\"><input type=\"submit\" id=\"group_assignment\" value=\"CREATE GROUP\" name=\"group\"></form></div></div></div></div></div>" +
                        "<script>document.getElementById(\"group_assignment\").disabled = "+group_created+";</script>" +
                        "<script>document.getElementById(\"inputGroupFile01\").disabled = "+!send+";</script>" +
                        "<script>document.getElementById(\"upload_file\").disabled = "+!send+";</script>" +
                        "<script>" +
                        "var input_file = document.getElementById( 'inputGroupFile01' );" +
                        "var label_file = document.getElementById( 'upload_label' );" +
                        "input_file.addEventListener( 'change', showFileName );" +
                        "function showFileName( event ) {" +
                        "var fileName = input_file.files[0].name;" +
                        "label_file.textContent = fileName;}</script>" +
                        "<script src=\"./bootstrap/js/bootstrap.bundle.js\"></script>" +
                        "<script src=\"./bootstrap/js/bootstrap.js\"></script>" +
                        "</body></html>");

                rs.close();
                if(project_id!=""){ request.getSession().setAttribute("project_id",project_id);}

            } catch (Exception e) {
                System.out.println(e);
            }

        }

    }

}