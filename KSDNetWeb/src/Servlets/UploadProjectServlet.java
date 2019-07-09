package Servlets;

import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "UploadProjectServlet", value="/UploadProject")
@MultipartConfig
public class UploadProjectServlet extends HttpServlet {

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

        String userid = (String) request.getSession().getAttribute("username");
        String coursename = (String) request.getSession().getAttribute("coursename");
        String project_id = (String) request.getSession().getAttribute("project_id");
        String group_id = "";
        try{
            Connection con = ds.getConnection();
            Part file = request.getPart("zipfile");
            InputStream fileContent = file.getInputStream();
            String fileName = Paths.get(file.getSubmittedFileName()).getFileName().toString();

            Date upload_date = new Date();
            SimpleDateFormat upload_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            Date parsedDate = upload_dateFormat.parse(upload_dateFormat.format(upload_date));
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

            PreparedStatement check_existing = con.prepareStatement("SELECT (SELECT group_id FROM groups INNER JOIN courses on groups.course_id = courses.course_id " +
                    "WHERE groups.student_id = '"+userid+"' AND courses.name = '"+coursename+"') FROM grade WHERE grade.project_id = '"+project_id+"';");
            ResultSet existing_groupid = check_existing.executeQuery();
            while(existing_groupid.next()){
                group_id += existing_groupid.getString("group_id");
            }
            check_existing.close();
            existing_groupid.close();
            if(group_id!=""){
                PreparedStatement upadte_file = con.prepareStatement("UPDATE grade SET filename = ?, file = ? WHERE grade.group_id='"+group_id+"';");
                upadte_file.setString(1,fileName);
                upadte_file.setBinaryStream(2,fileContent);
                upadte_file.executeUpdate();
                upadte_file.close();
            }else{
                PreparedStatement st3 = con.prepareStatement("INSERT INTO grade (group_id, project_id, upload_date, filename, file) VALUES(" +
                        "(SELECT group_id FROM groups INNER JOIN courses on groups.course_id = courses.course_id WHERE groups.student_id = '"+userid+"' AND courses.name = '"+coursename+"')," +
                        "?,?,?,?);");
                st3.setString(1, project_id);
                st3.setTimestamp(2, timestamp);
                st3.setString(3,fileName);
                st3.setBinaryStream(4,fileContent);
                st3.executeUpdate();
                st3.close();
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/StudentHomepage");
            dispatcher.forward(request, response);
            con.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
