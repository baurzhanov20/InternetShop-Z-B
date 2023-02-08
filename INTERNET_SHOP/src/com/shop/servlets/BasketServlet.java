package com.shop.servlets;

import com.shop.db.DBManager;
import com.shop.entities.Baskets;
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

@WebServlet("/basket")
public class BasketServlet extends HttpServlet {

    private DBManager dbManager;

    public void init(){
        dbManager = new DBManager();
        dbManager.connect();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String redirect = "?error";

        Users user = (Users)request.getSession().getAttribute("USER_SESSION");

        if(user!=null) {

            String operation = request.getParameter("operation");

            if(operation!=null&&operation.equals("add")){

                String itemIdString = request.getParameter("item_id");

                Long itemId = null;

                try{

                    itemId = Long.parseLong(itemIdString);

                }catch (Exception e){

                }

                if(itemId!= null) {

                    Items item = dbManager.getItem(itemId);

                    if(item!=null){
                        dbManager.addBasket(new Baskets(null, user, item));
                        redirect = "readitem?id="+itemId;

                    }else{

                        redirect = "/";

                    }

                }else{

                    redirect = "/";

                }

            }else if(operation!=null&&operation.equals("delete")){

                String idString = request.getParameter("id");
                Long id = Long.parseLong(idString);

                dbManager.deletBasket(id);

                redirect = "basket";

            }

        }

        response.sendRedirect(redirect);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Users userData = (Users)request.getAttribute("USER_DATA");

        if(userData!=null){

            ArrayList<Baskets> basket = dbManager.getBasketsByUser(userData);
            request.setAttribute("baskets", basket);
            request.getRequestDispatcher("basket.jsp").forward(request, response);

        }else{
            response.sendRedirect("/");
        }

    }
}
