package Servlets;

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
import java.sql.SQLException;

@WebServlet(name = "GroupGradeServlet", value="/GroupMembers")
public class GroupGradeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String group = "";
        String course = "";
        String projectid =(String) request.getSession().getAttribute("projectid");
        //Sets group id for session if its not set yet
        if(request.getParameter("group")!=null){
            group =request.getParameter("group");//gets group id from form
            request.getSession().setAttribute("groupid",group);//sets group id in session
            course = (String)request.getSession().getAttribute("courseid");//gets course id from session
        }
        else{
            group = (String)request.getSession().getAttribute("groupid");//gets group id from session
        }
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Teacher Homepage</title>" +
                "<link href=\"./bootstrap/css/bootstrap.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-grid.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-reboot.css\" rel=\"stylesheet\"><link rel=\"stylesheet\" href=\"./bootstrap/css/bootstrap.css\">" +
                "</head><body><div class=\"container d-flex justify-content-center\">\n" +
                "<div class=\"shadow p-3 mb-5 bg-white rounded\">\n" +
                "<div class=\"card text-center \" style=\"width: 45rem;\"><div class=\"card-body\"><div>" +
                "<div style=\"float: left;\"><form action=\"/GradingTeacher\" method=\"post\">" +
                "<input name=\"backbutton\" type=\"submit\" value=\"Go Back\"></form></div>" +
                "<h5 class=\"card-title\">Members of "+group+"</h5>\n" +
                "<div class = \"col\">\n");
        try{
            if(request.getParameter("insert") !=null){
                //insert grade and adds it to the sum
                int grade = Integer.parseInt(request.getParameter("grade"));
                GradeMapper gm = new GradeMapper();
                gm.insertGrade(group,projectid,grade);
                request.getSession().removeAttribute("insert");
                int sum =gm.SumofGrades(group);
                GroupMapper groupmap = new GroupMapper();
                groupmap.insertTotalgrade(group,sum);
                RequestDispatcher rs = request.getRequestDispatcher("/GradingTeacher");
                rs.forward(request,response);
            }
            GroupMapper grm = new GroupMapper();
            ResultSet Rs = grm.GetID(group,course);//get student ID within group
            PrintResults(Rs,out,projectid);
        }catch(Exception e){
        }
    }
    //added third parameter
    protected void PrintResults(ResultSet rs,PrintWriter out,String projectid) {
        try {
            out.println("<ul class=\"list-group list-group-flush\">");
            while (rs.next()){
                out.println("<li class=\"list-group-item list-group-item-action\">"+rs.getString("student_id")+"</li>");
            }
            ProjectMapper pm = new ProjectMapper();
            out.println("</ul><br>" +
                    "<form method=\"post\" action=\"/GroupMembers\">"+
                    "<input required type=\"number\" name=\"grade\" min=\"0\" max="+Integer.parseInt(pm.MaxGrade(projectid).getString("max_grade"))+"><br><br><input type=\"submit\" value=\"InsertGrade\" name=\"insert\"><br>" +
                    "</form>"+
                    "</div></div></div></div></div>" +
                    "<script src=\"./bootstrap/js/bootstrap.bundle.js\" ></script>" +
                    "<script src=\"./bootstrap/js/bootstrap.js\" ></script>" +
                    "</body>" +
                    "</html>");
        } catch (SQLException e){
        }
    }

}
