package com.shop.servlets;

import com.shop.db.DBManager;
import com.shop.entities.Users;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signin")
public class SignInServlet extends HttpServlet {

    private DBManager dbManager;

    public void init(){

        dbManager = new DBManager();
        dbManager.connect();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String message = "";

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(email!=null&&!email.trim().equals("")&&password!=null&&!password.trim().equals("")){

            Users user = dbManager.getUser(new Users(null, email, null, null, 0));

            if(user!=null){

                if(user.getPassword().equals(password)){

                    request.getSession().setAttribute("USER_SESSION", user);

                    String remember = request.getParameter("remember");

                    if(remember!=null&&remember.equals("remember")){

                        String rememberUserToken = DigestUtils.sha1Hex(user.getEmail()+user.getPassword()+"bitlab");
                        Cookie cookie = new Cookie("rememberUser", rememberUserToken);
                        cookie.setMaxAge(3*3600*24);

                        response.addCookie(cookie);

                    }

                }else{

                    message = "password";

                }

            }else{

                message = "user";

            }

        }else{

            message = "uncomplete";

        }

        response.sendRedirect("/?"+message);

    }
}
