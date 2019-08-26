package Servlets;

import Classes.StudentMapper;
import Classes.TeacherMapper;

import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@WebServlet(name = "LoginServlet",value ="/Login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8"); //θέτει τον τύπο περιεχομένου της απάντησης που αποστέλλεται στον πελάτη, εάν η απάντηση δεν έχει δεσμευτεί ακόμα
        request.setCharacterEncoding("UTF-8"); //κωδικοποίηση χαρακτήρων request
        response.setCharacterEncoding("UTF-8");

        String userid = request.getParameter("userid"); //επιστρέφει την τιμή σε μορφή string που έγραψε ο client στο πεδίο username
        String pass = request.getParameter("pass");
        HttpSession session=request.getSession(); //δημιουργείται session και δεσμεύεται με το request
        try{
            if(userid.charAt(0) == 'S'){
                StudentMapper sm = new StudentMapper();
                if(sm.login(userid,pass)) {
                    session.setAttribute("username", userid);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/StudentHomepage");
                    dispatcher.forward(request, response);
                }else{
                        response.sendRedirect("index.html");
                        return;
                }
            }else if(userid.charAt(0) == 'T'){
                TeacherMapper tm = new TeacherMapper();
                if(tm.login(userid,pass)) {//if credentials exist
                    session.setAttribute("username", userid);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TeacherHomepage");
                    dispatcher.forward(request, response);
                }else {
                    response.sendRedirect("index.html");
                    return;
                }
            }
            else{
                response.sendRedirect("index.html");
                return;
            }
        }catch (Exception e){
                response.sendRedirect("index.html");
                return;
        }
    }


}
