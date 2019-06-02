package Servlets;

import javax.naming.InitialContext;
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
import javax.servlet.RequestDispatcher;


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
        int number_projects = Integer.parseInt(request.getParameter("number_projects"));

        try {

            Connection con = ds.getConnection();
            PreparedStatement st = con.prepareStatement(" insert into courses (course_id, teacher_id, name, number_projects) values (?, ?, ?, ?)");
            st.setString(1, course_id);
            st.setString(2, userid);
            st.setString(3, name);
            st.setInt(4, number_projects);

            st.executeUpdate();
            st.close();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TeacherHomepage");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            PrintWriter out = response.getWriter();	//για εκτύπωση στην html
            String title = "Course already exists";
            String docType ="<!doctype html public\">\n";
            out.println(docType +
                    "<html>\n" +
                    "<head><title>" + title + "</title><style>input[type=button]{margin:50px 42% auto;font-size:10pt;font-weight:bold;}" +
                    "</style></head>\n" +
                    "<body bgcolor = \"#f0f0f0\">\n" +
                    "<h1 align = \"center\">" + title + "</h1>\n" +
                    "<h3 align=\"center\">Courseid already used!!!</h3>"+
                    "<input onclick=\"location.href='new_course.html'\" type=\"button\" value=\"GO_BACK_TO_ADD_COURSE\">"+"</body></html>");
            return;
        }
    }
}
