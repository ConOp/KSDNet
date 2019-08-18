package Servlets;


import Classes.GradeMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


@WebServlet(name = "DownloadProjectServlet", value="/DownloadProject")
public class DownloadProjectServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseid = "";
        courseid = (String) request.getSession().getAttribute("courseid");
        try {
            GradeMapper cm = new GradeMapper();

            ResultSet rs= cm.DownloadProject(courseid);

            while (rs.next()) {
                String filename = rs.getString("filename");
                byte[] file = rs.getBytes("file");
                System.out.println("File Name: " + filename);


                String contentType = this.getServletContext().getMimeType(filename);
                System.out.println("Content Type: " + contentType);

                response.setHeader("Content-Type", contentType);
                response.setContentLength(file.length);
                response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
                response.getOutputStream().write(file);


            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
