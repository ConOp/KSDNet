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
import java.sql.ResultSet;

@WebServlet(name = "GradeServlet", value="/GradingTeacher")
public class GradeServlet extends HttpServlet {
    private DataSource ds = null;

    public void init() throws ServletException { //φορτώνεται ο servlet και καλείται η init, για αρχικοποιήσεις και σύνδεση με τη βάση
        try {
            InitialContext ctx = new InitialContext(); //πόροι για datasource
            ds = (DataSource)ctx.lookup("java:comp/env/jdbc/postgres"); //lookup δεσμεύει το αντικείμενο ds τύπου datasource με το string που θέλουμε
        }catch(Exception e) {
            throw new ServletException(e.toString());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8"); //θέτει τον τύπο περιεχομένου της απάντησης που αποστέλλεται στον πελάτη, εάν η απάντηση δεν έχει δεσμευτεί ακόμα
        request.setCharacterEncoding("UTF-8"); //κωδικοποίηση χαρακτήρων request
        response.setCharacterEncoding("UTF-8");

        String testname = "C57832";//(String)request.getParameter();
        ResultSet Rs=null;
        try{
            Connection con = ds.getConnection();
            PreparedStatement st = con.prepareStatement("SELECT  group_id,totalgrade FROM groups WHERE course_id='"+testname+"'"); //παίρνουμε το userid από τη βάση
            Rs = st.executeQuery();
        }catch(Exception e){}


        PrintWriter out=response.getWriter();
        out.println("    <!DOCTYPE html>\n" +
                "    <html>\n" +
                "    <head>\n" +
                "     <title>Table with database</title>\n" +
                "     <style>\n" +
                "      table {\n" +
                "       border-collapse: collapse;\n" +
                "       width: 100%;\n" +
                "       color: #588c7e;\n" +
                "       font-family: monospace;\n" +
                "       font-size: 25px;\n" +
                "       text-align: left;\n" +
                "         } \n" +
                "      th {\n" +
                "       background-color: #588c7e;\n" +
                "       color: white;\n" +
                "        }\n" +
                "      tr:nth-child(even) {background-color: #f2f2f2}\n" +
                "     </style>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "     <table>\n" +
                "     <tr>\n" +
                "      <th>Group ID</th> \n" +
                "      <th>Grade</th> \n" +
                "     </tr>\n");
        try{
            while(Rs.next()) {
                out.println("<tr><td>"+Rs.getString("group_id")+"</td><td>"+Rs.getString("totalgrade")+"</td><td>\n");
                System.out.println(Rs.getString(("group_id")+" | "+ Rs.getString("totalgrade")));
            }
        }catch(Exception e){
            System.out.println("Shit");
        }

        out.println("</table>\n" +
                "    </body>\n" +
                "    </html>");

    }
}
