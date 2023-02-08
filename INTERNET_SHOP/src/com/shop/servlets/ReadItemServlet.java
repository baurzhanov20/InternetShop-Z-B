package com.shop.servlets;

import com.shop.db.DBManager;
import com.shop.entities.Categories;
import com.shop.entities.Items;
import com.shop.entities.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/readitem")
public class ReadItemServlet extends HttpServlet {

    private DBManager dbManager;

    public void init(){
        dbManager = new DBManager();
        dbManager.connect();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String itemId = request.getParameter("id");
        Long id = null;

        try {

            id = Long.parseLong(itemId);

        }catch (Exception e){

        }

        String name = request.getParameter("name");
        int amount = Integer.parseInt(request.getParameter("amount"));
        int price = Integer.parseInt(request.getParameter("price"));
        Long categoryId = Long.parseLong(request.getParameter("category_id"));

        dbManager.saveItem(new Items(id, name, price, amount, new Categories(categoryId, null), null ));

        response.sendRedirect("readitem?id="+id);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Users userData = (Users)request.getAttribute("USER_DATA");
        String jspPage = "404.jsp";

        String itemId = request.getParameter("id");
        Long id = null;

        try {

            id = Long.parseLong(itemId);

        }catch (Exception e){

        }

        Items item = null;

        if(id!=null){
            item = dbManager.getItem(id);
        }

        if(item!=null) {

            if (userData != null) {

                if (userData.getRoleId() == 1) {

                    ArrayList<Categories> categories = dbManager.getAllCategories();
                    request.setAttribute("categories", categories);
                    request.setAttribute("item", item);
                    request.getRequestDispatcher("edititem.jsp").forward(request, response);

                } else {

                    request.setAttribute("item", item);
                    request.getRequestDispatcher("readitem.jsp").forward(request, response);

                }

            } else {

                request.setAttribute("item", item);
                request.getRequestDispatcher("readitem.jsp").forward(request, response);

            }

        }else{

            request.getRequestDispatcher("404.jsp").forward(request, response);

        }

    }
}
