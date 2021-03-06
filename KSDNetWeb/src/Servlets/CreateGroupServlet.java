package Servlets;

import Classes.CourseMapper;
import Classes.GroupMapper;
import Classes.StudentMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

@WebServlet(name = "CreateGroupServlet",value = "/CreatGroup")
public class CreateGroupServlet extends HttpServlet {

    private  int counter;

    public int Counter(int _counter) { //getter and setter
        this.counter = _counter;
        return counter;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userid = (String) request.getSession().getAttribute("username");//gets user id from session
        String coursename =(String) request.getSession().getAttribute("coursename");//gets coursename from session
        //if user is not a student
        if (userid.charAt(0) != 'S') {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            boolean flag = false;
            boolean duplicate_exists = false;
            PrintWriter out = response.getWriter();
            String[] guserids;
            String groupid = userid;
            try{
                out.println("<!DOCTYPE HTML>" +
                        "<html>" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<title>Group assignmet</title>" +
                        "<link href=\"./bootstrap/css/bootstrap.css\" rel=\"stylesheet\">" +
                        "<link href=\"./bootstrap/css/bootstrap-grid.css\" rel=\"stylesheet\">" +
                        "<link href=\"./bootstrap/css/bootstrap-reboot.css\" rel=\"stylesheet\"><link rel=\"stylesheet\" href=\"./bootstrap/css/bootstrap.css\">" +
                        "</head><body><div class=\"container d-flex justify-content-center\">" +
                        "<div class=\"shadow p-3 mb-5 bg-white rounded\">" +
                        "<div class=\"card text-center \" style=\"width: 45rem;\"><div class=\"card-body\">" +
                        "<div class=\"text-left \">" +
                        "<form method=\"post\" action=\"/StudentHomepage\"><input name=\"backbutton\" type=\"submit\" value=\"Go Back to Home\"></form></div>" +
                        "<h5 class=\"card-title\">Create Group</h5><br>" +
                        "<h6 class=\"card-subtitle mb-2 text-muted\">Choose carefully your team</h6><div class = \"col\">" +
                        "<form method=\"post\" action=\"/CreatGroup\"><br>");

                CourseMapper cm = new CourseMapper();
                ResultSet rs = cm.groupmembers(coursename);
                while(rs.next()){
                       counter = Counter(Integer.parseInt(rs.getString("groupmembers"))) - 1; //except the user
                }
                for (int i=1;i<=counter;i++){
                            out.println("<input name =\""+i+"\" placeholder=\"SXXXXX\" type=\"text\" maxlength=\"6\" " +
                            "pattern=\"^[S][0-9][0-9][0-9][0-9][0-9]\" title=\"First character must be S followed by 5 digits\" required><br>");
                }

                out.println("<br><input type=\"submit\" value=\"TEAM ASSIGNMENT\" name=\"assign_group\"></form></div></div></div></div></div>" +
                        "<script src=\"./bootstrap/js/bootstrap.bundle.js\"></script>" +
                        "<script src=\"./bootstrap/js/bootstrap.js\"></script></body></html>");

                rs.close();
                guserids = new String[counter];
                if(request.getParameter("assign_group")!=null) {
                    GroupMapper gm = new GroupMapper();
                    if(counter!=0) {                                    //group assignment with more than one members
                        for (int i = 1; i <= counter; i++) {
                            guserids[i - 1] = request.getParameter(Integer.toString(i));
                        }
                        for (int i = 0; i < guserids.length; i++) {
                            groupid += guserids[i];
                            String temp = guserids[i];
                            if (temp.equals(userid)){
                                duplicate_exists = true;
                            }
                        }
                        StudentMapper students = new StudentMapper();
                        ResultSet i_rs = students.check_userids(guserids); //check if students exist in db

                        while (i_rs.next()) {
                            if (i_rs.getInt("count") == counter) {
                                flag = true;
                            }
                        }
                        i_rs.close();

                        if (flag && duplicate_exists==false) {
                            gm.group_assign(groupid,userid,guserids,coursename);

                        } else {
                            response.sendRedirect("invalid_group.html");
                            return;
                        }
                    }else{                                              //group assignment with one member
                        gm.single_project(userid,coursename);
                    }
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Student_CourseHomepage?coursename=" + coursename);
                    dispatcher.forward(request, response);
                }

            }catch (Exception e){
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }

    }

}
