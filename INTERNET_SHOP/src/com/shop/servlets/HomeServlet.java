package com.shop.servlets;

import com.shop.db.DBManager;
import com.shop.entities.Items;
import com.shop.entities.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private DBManager dbManager;

    public void init(){
        dbManager = new DBManager();
        dbManager.connect();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Users user = (Users)request.getAttribute("USER_DATA");

        if(user!=null){
            if(user.getRoleId()==1){

                ArrayList<Items> allItems = dbManager.getAllItems();
                request.setAttribute("items", allItems);

                request.getRequestDispatcher("admin.jsp").forward(request,response);

            }else {

                ArrayList<Items> allItems = dbManager.getAllItems();
                request.setAttribute("items", allItems);

                request.getRequestDispatcher("profile.jsp").forward(request, response);
            }
        }else{

            ArrayList<Items> allItems = dbManager.getAllItems();
            request.setAttribute("items", allItems);

            request.getRequestDispatcher("index.jsp").forward(request,response);
        }

    }
}
