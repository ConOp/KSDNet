package Servlets;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(name = "AddCourseServlet",value ="/Add_course")

public class AddCourseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private DataSource ds = null;
    public void init() throws ServletException { //φορτώνεται ο servlet και καλείται η init, για αρχικοποιήσεις και σύνδεση με τη βάση
        try {
            InitialContext ctx = new InitialContext(); //πόροι για datasource
            ds = (DataSource)ctx.lookup("java:comp/env/jdbc/postgres"); //lookup δεσμεύει το αντικείμενο ds τύπου datasource με το string που θέλουμε
        }catch(Exception e) {
            throw new ServletException(e.toString());
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userid = (String)request.getSession().getAttribute("username");
        response.setContentType("text/html");
        request.setCharacterEncoding("UTF-8"); //κωδικοποίηση χαρακτήρων request
        response.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String course_id = request.getParameter("course_id");
        String number_projects = request.getParameter("number_projects");

        try {

            Connection con = ds.getConnection();
            PreparedStatement st = con.prepareStatement(" insert into courses (course_id, teacher_id, name, number_projects)\"\n" +
                            "        + \" values (?, ?, ?, ?)");
            st.setString(1, course_id);
            st.setString(2, userid);
            st.setString(3, name);
            st.setString(4, number_projects);

            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
