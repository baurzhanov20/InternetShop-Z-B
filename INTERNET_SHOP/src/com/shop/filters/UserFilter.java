package com.shop.filters;

import com.shop.db.DBManager;
import com.shop.entities.Users;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class UserFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest)req;
        HttpSession session = request.getSession();

        Users sessionTokenUser = (Users)session.getAttribute("USER_SESSION");

        if(sessionTokenUser==null){

            Cookie[] cookies = ((HttpServletRequest) req).getCookies();
            for(Cookie c : cookies){
                if(c.getName().equals("rememberUser")){

                    String token = c.getValue();
                    Users tokenUser = dbManager.getUser(token);
                    if(tokenUser!=null){
                        session.setAttribute("USER_SESSION", tokenUser);
                    }

                    break;
                }
            }
        }

        Users user = (Users)session.getAttribute("USER_SESSION");
        Users userData = null;

        if(user!=null){

            userData = dbManager.getUser(new Users(null, user.getEmail(), null, null, 0));

            if(userData!=null){
                if(userData.getPassword().equals(user.getPassword())){

                    req.setAttribute("USER_DATA", userData);

                }else{
                    session.removeAttribute("USER_SESSION");
                }
            }else{
                session.removeAttribute("USER_SESSION");
            }

        }

        chain.doFilter(req, resp);
    }

    private DBManager dbManager;

    public void init(FilterConfig config) throws ServletException {
        dbManager = new DBManager();
        dbManager.connect();
    }

}
