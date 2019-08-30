package Servlets;

import Classes.GradeMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "GradeServlet", value="/GradingTeacher")
public class GradeServlet extends HttpServlet {
    private DataSource ds = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8"); //θέτει τον τύπο περιεχομένου της απάντησης που αποστέλλεται στον πελάτη, εάν η απάντηση δεν έχει δεσμευτεί ακόμα
        request.setCharacterEncoding("UTF-8"); //κωδικοποίηση χαρακτήρων request
        response.setCharacterEncoding("UTF-8");
        PrintWriter  out=response.getWriter();
        String projectid;
        if(request.getSession().getAttribute("projectid")==null) {
            projectid = request.getParameter("ProjectID");
            request.getSession().setAttribute("projectid", projectid);
        }
        else{
            projectid=(String)request.getSession().getAttribute("projectid");
        }



        out.println("<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Grade assignment of groups</title>" +
                "<link href=\"./bootstrap/css/bootstrap.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-grid.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-reboot.css\" rel=\"stylesheet\"><link rel=\"stylesheet\" href=\"./bootstrap/css/bootstrap.css\">" +
                "</head><body><div class=\"container d-flex justify-content-center\">\n" +
                "<div class=\"shadow p-3 mb-5 bg-white rounded\">\n" +
                "<div class=\"card text-center \" style=\"width: 45rem;\"><div class=\"card-body\"><div>" +
                "<div style=\"float: left;\"><form action=\"/TCourse\" method=\"post\">" +
                "<input name=\"backbutton\" type=\"submit\" value=\"Go Back\"></form></div>" +
                "<h5 class=\"card-title\">List of Groups</h5>\n" +
                "<div class = \"col\">\n" +
                "<form method=\"post\" action=\"/GroupMembers\"><ul class=\"list-group list-group-flush\">");


        try{
            GradeMapper gm = new GradeMapper();
            ResultSet Rs = gm.GetGroup(projectid);
            PrintResults(Rs,out,projectid);

        }catch(Exception e){

        }
        out.println("</div></div></div></div></div>" +
                "<script src=\"./bootstrap/js/bootstrap.bundle.js\" ></script>" +
                "<script src=\"./bootstrap/js/bootstrap.js\" ></script>" +
                "</body>" +
                "</html>");
    }
    protected void PrintResults(ResultSet rs,PrintWriter out,String projectid) {
        try {
            int i =0;
            out.println("<form method=\"POST\" action=\"/GroupMembers\">" +
                    "<ul class=\"list-group list-group-flush\">");
            while (rs.next()){
                out.println("<input type=\"submit\" name=\"group\" id="+i+" class=\"list-group-item list-group-item-action\" value=\""+rs.getString("group_id")+"\">");
                i++;
            }
            out.println("</ul></form><br><br>");
            GradeMapper gm = new GradeMapper();
            if(gm.project_exist_for_grade(projectid)){
                out.println("<form action=\"/DownloadProject\" method=\"Post\">"+
                        "<input type=\"submit\" name=\"DownloadProject\" value=\"Download Project\">"  +
                        "</form>");
            }
            else{
                out.println("<form action=\"/DownloadProject\" method=\"Post\">"+
                        "<input type=\"submit\" name=\"DownloadProject\" value=\"Download Project\" disabled>"  +
                        "</form>");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
