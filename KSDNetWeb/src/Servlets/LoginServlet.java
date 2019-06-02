package Servlets;

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

    private boolean flag = false;
    private static byte[] salt;
    private String securePassword = null;

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8"); //θέτει τον τύπο περιεχομένου της απάντησης που αποστέλλεται στον πελάτη, εάν η απάντηση δεν έχει δεσμευτεί ακόμα
        request.setCharacterEncoding("UTF-8"); //κωδικοποίηση χαρακτήρων request
        response.setCharacterEncoding("UTF-8");

        String userid = request.getParameter("userid"); //επιστρέφει την τιμή σε μορφή string που έγραψε ο client στο πεδίο username
        String pass = request.getParameter("pass");
        String user_type = "";
        String id = "";
        HttpSession session=request.getSession(); //δημιουργείται session και δεσμεύεται με το request

        if(userid.charAt(0) == 'S'){
            user_type = "students";
            id = "student_id";
        }else if(userid.charAt(0) == 'T'){
            user_type = "teachers";
            id = "teacher_id";
        }else{
            response.sendRedirect("index.html"); //an to userid 3ekinaei apo opoiodhpote allo xarakthra
            return;
        }

        try{
            Connection con = ds.getConnection();
            PreparedStatement sm = con.prepareStatement("SELECT "+id+", password, salt FROM "+user_type+" where "+id+" = '"+ userid +"';");
            ResultSet Rs1 = sm.executeQuery();
            if(Rs1.next()) {
                salt = Rs1.getBytes("salt");
                securePassword = RegisterServlet.SecurePassword(pass,salt); /*υπολογισμός του hashed&salted password με βάση τα στοιχεία του χρήστη(pass),
								 										και το salt της βάσης, αφού υπάρχει χρήστης με τέτοιο id*/

                if(userid.equals(Rs1.getString(id))&&securePassword.equals(Rs1.getString("password"))){ //έλεγχος έγκυρου password και username
                    session.setAttribute("username", userid);

                    if(user_type=="teachers"){
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/TeacherHomepage");
                        dispatcher.forward(request, response);
                    }
                    else{
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/StudentHomepage");
                        dispatcher.forward(request, response);
                    }
                }else {
                    response.sendRedirect("index.html"); //αν δώσει σωστό username και λάθος κωδικό
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
