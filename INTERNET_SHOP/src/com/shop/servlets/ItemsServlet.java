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

@WebServlet("/items")
public class ItemsServlet extends HttpServlet {

    private DBManager dbManager;

    public void init(){
        dbManager = new DBManager();
        dbManager.connect();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String redirect = "?error";

        Users user = (Users)request.getSession().getAttribute("USER_SESSION");

        if(user!=null&&user.getRoleId()==1) {

            String operation = request.getParameter("operation");

            if(operation!=null&&operation.equals("add")){

                String name = request.getParameter("name");
                String price = request.getParameter("price");
                String amount = request.getParameter("amount");
                String category = request.getParameter("category_id");

                int priceInt = 0;
                int amountInt = 0;
                Long categoryId = 0L;

                try{

                    priceInt = Integer.parseInt(price);
                    amountInt = Integer.parseInt(amount);
                    categoryId = Long.parseLong(category);

                }catch (Exception e){

                }

                if(name!= null&&!name.equals("")) {

                    dbManager.addItem(new Items(null, name, priceInt, amountInt, new Categories(categoryId, null), null ));
                    redirect = "items?success";

                }else{

                    redirect = "items?error";

                }

            }else if(operation!=null&&operation.equals("delete")){

                String categoryIdString = request.getParameter("id");
                Long categoryId = Long.parseLong(categoryIdString);

                dbManager.deleteItem(categoryId);

                redirect = "items?deleted";

            }

        }

        response.sendRedirect(redirect);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Users userData = (Users)request.getAttribute("USER_DATA");

        if(userData!=null){

            if(userData.getRoleId()==1){

                ArrayList<Categories> categories = dbManager.getAllCategories();
                request.setAttribute("categories", categories);
                ArrayList<Items> allItems = dbManager.getAllItems();
                request.setAttribute("items", allItems);

                request.getRequestDispatcher("items.jsp").forward(request, response);

            }else{
                request.getRequestDispatcher("403.jsp").forward(request, response);
            }

        }else{
            response.sendRedirect("/");
        }

    }
}
