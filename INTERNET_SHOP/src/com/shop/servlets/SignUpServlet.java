package com.shop.servlets;

import com.shop.db.DBManager;
import com.shop.entities.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    private DBManager dbManager;

    public void init(){

        dbManager = new DBManager();
        dbManager.connect();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String message = "error";

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rePassword = request.getParameter("re-password");
        String fullName = request.getParameter("full_name");

        if(email!=null&&!email.trim().equals("")&&password!=null&&!password.trim().equals("")&&rePassword!=null&&!rePassword.trim().equals("")){

            if(password.equals(rePassword)){

                Users exists = dbManager.getUser(new Users(null, email, null, null, 0));

                if(exists==null){

                    dbManager.addUser(new Users(null, email, password, fullName, 2));
                    message = "success";

                }else{

                    message = "exists";

                }

            }else{

                message = "mismatch";

            }

        }else{

            message = "uncomplete";

        }

        response.sendRedirect("signup?"+message);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}
