package com.library.project.servlets;

import java.io.IOException;

import com.library.project.contracts.HttpBaseController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("")
public class HomeServlet extends HttpBaseController {
    
    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void index(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {
        this.view("views/home.jsp", request, response);
    }
}
