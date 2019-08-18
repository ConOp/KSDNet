package Servlets;

import Classes.CourseMapper;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

@WebServlet(name = "StudentServlet", value="/StudentHomepage")
public class StudentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8"); //θέτει τον τύπο περιεχομένου της απάντησης που αποστέλλεται στον πελάτη, εάν η απάντηση δεν έχει δεσμευτεί ακόμα
        request.setCharacterEncoding("UTF-8"); //κωδικοποίηση χαρακτήρων request
        response.setCharacterEncoding("UTF-8");

        String userid = (String)request.getSession().getAttribute("username");

        PrintWriter  out=response.getWriter();
        out.println("<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Student Homepage</title>" +
                "<link href=\"./bootstrap/css/bootstrap.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-grid.css\" rel=\"stylesheet\">" +
                "<link href=\"./bootstrap/css/bootstrap-reboot.css\" rel=\"stylesheet\"><link rel=\"stylesheet\" href=\"./bootstrap/css/bootstrap.css\">" +
                "</head><body><div class=\"container d-flex justify-content-center\">\n" +
                "<div class=\"shadow p-3 mb-5 bg-white rounded\">\n" +
                "<div class=\"card text-center \" style=\"width: 45rem;\"><div class=\"card-body\">\n" +
                "<h5 class=\"card-title\">Course List</h5>\n" +
                "<h6 class=\"card-subtitle mb-2 text-muted\">Selected Courses</h6><div class = \"col\">\n" +
                "<form method=\"post\" action=\"/Student_CourseHomepage\"><ul class=\"list-group list-group-flush\">");
        try {
            CourseMapper cm = new CourseMapper();
            ResultSet rs=cm.get_allcourses();
            PrintResults(rs,out);

            if(request.getParameter("logout")!=null) {

                request.getSession().removeAttribute("username");
                request.getSession().invalidate();
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.sendRedirect("index.html");
            }

        }
        catch (Exception e){

        }

    }


    protected void PrintResults(ResultSet rs,PrintWriter out) {

        try {
            while (rs.next()){

                out.println("<input type=\"submit\"  name=\"coursename\" value=\""+rs.getString("name")+"\" class=\"list-group-item list-group-item-action\"> ");
            }
            out.println("</ul></form>\n" +
                    "<br><form method=\"post\" action=\"/StudentHomepage\"><input type=\"submit\" id=\"log\" value=\"Logout\" name=\"logout\">\n" +
                    "</form></div></div></div></div></div>" +
                    "<script src=\"./bootstrap/js/bootstrap.bundle.js\" ></script>" +
                    "<script src=\"./bootstrap/js/bootstrap.js\" ></script>" +
                    "</body>" +
                    "</html>");

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
