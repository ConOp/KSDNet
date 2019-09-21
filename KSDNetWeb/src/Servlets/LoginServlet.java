package Servlets;

import Classes.StudentMapper;
import Classes.TeacherMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(name = "LoginServlet",value ="/Login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String userid = request.getParameter("userid"); //gets user id from form
        String pass = request.getParameter("pass");//gets user pass from form
        HttpSession session=request.getSession(); //creates a session for the user
        try{
            //if the user is student
            if(userid.charAt(0) == 'S'){
                StudentMapper sm = new StudentMapper();
                //if credentials are correct
                if(sm.login(userid,pass)) {
                    session.setAttribute("username", userid);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/StudentHomepage");
                    dispatcher.forward(request, response);
                }else {
                    response.sendRedirect("index.html");
                    return;
                }
            //if the user is teacher
            }else if(userid.charAt(0) == 'T'){
                TeacherMapper tm = new TeacherMapper();
                //if credentials are correct
                if(tm.login(userid,pass)) {
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
