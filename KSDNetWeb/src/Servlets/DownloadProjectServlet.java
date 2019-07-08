package Servlets;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Blob;
import java.io.InputStream;



@WebServlet(name = "DownloadProjectServlet", value="/DownloadProject")
public class DownloadProjectServlet extends HttpServlet {
    private DataSource ds = null;
    public void init() throws ServletException { //φορτώνεται ο servlet και καλείται η init, για αρχικοποιήσεις και σύνδεση με τη βάση
        try {
            InitialContext ctx = new InitialContext(); //πόροι για datasource
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/postgres"); //lookup δεσμεύει το αντικείμενο ds τύπου datasource με το string που θέλουμε
        } catch (Exception e) {
            throw new ServletException(e.toString());
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Courseid = "";
        Courseid = (String) request.getSession().getAttribute("courseid");
        try {
            Connection con = ds.getConnection();
            PreparedStatement st = con.prepareStatement("select filename,file from grade  inner join projects on grade.project_id= projects.project_id where projects.course_id=?");
            st.setString(1, Courseid);
            ResultSet rs = st.executeQuery();
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
            st.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
