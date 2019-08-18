package Servlets;

import Classes.GradeMapper;
import Classes.GroupMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "UploadProjectServlet", value="/UploadProject")
@MultipartConfig
public class UploadProjectServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userid = (String) request.getSession().getAttribute("username");
        String coursename = (String) request.getSession().getAttribute("coursename");
        String project_id = (String) request.getSession().getAttribute("project_id");
        String group_id = "";
        try{
            Part file = request.getPart("zipfile");
            InputStream fileContent = file.getInputStream();
            String fileName = Paths.get(file.getSubmittedFileName()).getFileName().toString();

            Date upload_date = new Date();
            SimpleDateFormat upload_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            Date parsedDate = upload_dateFormat.parse(upload_dateFormat.format(upload_date));
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            GroupMapper gm = new GroupMapper();

            ResultSet existing_groupid = gm.CheckExistingProject(userid,coursename,project_id);
            while(existing_groupid.next()){
                group_id += existing_groupid.getString("group_id");
            }

            existing_groupid.close();

            if(group_id!=""){
                GradeMapper gradem=new GradeMapper();
                gradem.Updatefile(fileName,fileContent,group_id);
            }else{
                GradeMapper gradem= new GradeMapper();
                gradem.Uploadfile(userid,coursename,project_id,timestamp,fileName,fileContent);
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/StudentHomepage");
            dispatcher.forward(request, response);
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
