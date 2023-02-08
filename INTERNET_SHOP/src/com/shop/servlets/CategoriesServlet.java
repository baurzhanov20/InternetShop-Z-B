package com.shop.servlets;

import com.shop.db.DBManager;
import com.shop.entities.Categories;
import com.shop.entities.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/categories")
public class CategoriesServlet extends HttpServlet {

    private DBManager dbManager;

    public void init(){
        dbManager = new DBManager();
        dbManager.connect();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Users userData = (Users)request.getAttribute("USER_DATA");

        if(userData!=null){

            if(userData.getRoleId()==1){

                ArrayList<Categories> categories = dbManager.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("categories.jsp").forward(request, response);

            }else{
                request.getRequestDispatcher("403.jsp").forward(request, response);
            }

        }else{
            response.sendRedirect("/");
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String redirect = "?error";

        Users user = (Users)request.getSession().getAttribute("USER_SESSION");

        if(user!=null&&user.getRoleId()==1) {

            String operation = request.getParameter("operation");

            if(operation!=null&&operation.equals("add")){

                String name = request.getParameter("name");

                if(name != null&&!name.equals("")) {

                    dbManager.addCategories(new Categories(null, name));
                    redirect = "categories?success";

                }else{

                    redirect = "categories?error";

                }

            }else if(operation!=null&&operation.equals("delete")){

                String categoryIdString = request.getParameter("id");
                Long categoryId = Long.parseLong(categoryIdString);

                dbManager.deleteCategory(categoryId);

                redirect = "categories?deleted";

            }

        }

        response.sendRedirect(redirect);
    }
}
