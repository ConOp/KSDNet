package Servlets;


import Classes.GradeMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@WebServlet(name = "DownloadProjectServlet", value="/DownloadProject")
public class DownloadProjectServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectid = "";
        projectid= (String) request.getSession().getAttribute("projectid");//gets project id from session
        try {
            GradeMapper cm = new GradeMapper();
            ResultSet rs= cm.DownloadProject(projectid);
            //Downloads all files from all groups
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream output = new ZipOutputStream(baos);
            while (rs.next()){
                String filename = rs.getString("filename");
                byte[] file = rs.getBytes("file");
                ZipEntry entry = new ZipEntry(filename);
                entry.setSize(file.length);
                output.putNextEntry(entry);
                output.write(file);
            }
            output.closeEntry();
            output.close();
            String contentType = this.getServletContext().getMimeType("application/zip");
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "inline; filename=\"" + projectid + ".zip\"");
            response.getOutputStream().write(baos.toByteArray());

        } catch (SQLException e) {
        }


    }
}
