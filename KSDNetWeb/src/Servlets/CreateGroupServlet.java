package Servlets;

import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "CreateGroupServlet",value = "/CreatGroup")
public class CreateGroupServlet extends HttpServlet {

    private  int counter;

    private DataSource ds = null;
    public void init() throws ServletException { //φορτώνεται ο servlet και καλείται η init, για αρχικοποιήσεις και σύνδεση με τη βάση
        try {
            InitialContext ctx = new InitialContext(); //πόροι για datasource
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/postgres"); //lookup δεσμεύει το αντικείμενο ds τύπου datasource με το string που θέλουμε
        } catch (Exception e) {
            throw new ServletException(e.toString());
        }
    }

    public int Counter(int _counter) { //getter and setter
        this.counter = _counter;
        return counter;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userid = (String) request.getSession().getAttribute("username");
        String coursename =(String) request.getSession().getAttribute("coursename");
        if (userid.charAt(0) != 'S') {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            boolean flag = false;
            PrintWriter out = response.getWriter();
            String[] guserids;
            String groupid = "";
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
                        "<h5 class=\"card-title\">Create Group</h5><br>" +
                        "<h6 class=\"card-subtitle mb-2 text-muted\">Choose your team members</h6><div class = \"col\">" +
                        "<form method=\"post\" action=\"/CreatGroup\"><br>");
                Connection con = ds.getConnection();
                PreparedStatement st = con.prepareStatement("select groupmembers from projects inner join courses on projects.course_id = courses.course_id where courses.name = '"+coursename+"';");
                ResultSet rs= st.executeQuery();

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
                st.close();
                rs.close();
                guserids = new String[counter];

                if(request.getParameter("assign_group")!=null) {

                    for (int i = 1; i <= counter; i++) { guserids[i-1] = request.getParameter(Integer.toString(i)); }
                    for (int i=0;i<guserids.length;i++){ groupid += guserids[i];}

                    //check below if the given userids for group assignment, get counter for the ones exist in database
                    String check_statement = "SELECT count(*) FROM students WHERE students.student_id in(";
                    for (int i=0;i<guserids.length;i++){
                        check_statement += "'"+guserids[i]+"'";
                        if(i==guserids.length-1){ check_statement+=");";}else{ check_statement += ",";}
                    }
                    PreparedStatement initial = con.prepareStatement(check_statement);
                    ResultSet i_rs = initial.executeQuery();
                    while(i_rs.next()){
                        if(i_rs.getInt("count")==counter){
                            flag = true;
                        }
                    }
                    i_rs.close();
                    initial.close();
                    if(flag){
                        String statement = "UPDATE students SET group_id='"+groupid+"' WHERE students.student_id IN ('"+userid+"'";

                        for (int i=0;i<guserids.length;i++){
                            statement += ",'"+guserids[i]+"'";
                            if(i==guserids.length-1){ statement+=");";}
                        }

                        PreparedStatement g = con.prepareStatement(statement);
                        g.executeUpdate();
                        g.close();
                        PreparedStatement p = con.prepareStatement("INSERT INTO groups(group_id,course_id) VALUES (?,(select course_id from courses where courses.name = '"+coursename+"'));");
                        p.setString(1, groupid);
                        p.executeUpdate();
                        p.close();
                        con.close();
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Student_CourseHomepage?coursename="+ coursename);
                        dispatcher.forward(request, response);

                    }else{ response.sendRedirect("invalid_group.html");}

                }

            }catch (Exception e){
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                System.out.println(e); //NA TO BGALOUME AUTO STO TELOS
            }
        }

    }


}
