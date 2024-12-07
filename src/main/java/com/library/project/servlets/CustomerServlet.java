package com.library.project.servlets;

import java.io.IOException;

import com.library.project.beans.Customer;
import com.library.project.classes.JsonResponse;
import com.library.project.contracts.HttpBaseController;
import com.library.project.factories.CustomerFactory;
import com.library.project.repositories.CustomerRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/customer/*")
public class CustomerServlet extends HttpBaseController {
    
    @Override
    protected void index(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {

        if (! this.ajax(request)) {
            this.view("views/customer.jsp", request, response);
        }
        
        JsonResponse jsonResponse;
        
        try (CustomerRepository repository = new CustomerRepository(entityManagerFactory)) {
            jsonResponse = new JsonResponse(201, repository.paginate(request, Customer.class));
        } catch (Exception e) {
            jsonResponse = new JsonResponse(419, e.getMessage());
        }

        this.json(jsonResponse, response);
    }
    
    @Override
    protected void store(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {

        JsonResponse jsonResponse;

        try (CustomerRepository repository = new CustomerRepository(entityManagerFactory)) {
            Customer customer = repository.findOrCreate(
                CustomerFactory.fromRequest(request)
            );
            
            jsonResponse = new JsonResponse(201, customer);
        } catch (Exception e) {
            jsonResponse = new JsonResponse(419, e.getMessage());
        }
        
        this.json(jsonResponse, response);
    }
    
    @Override
    protected void show(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        
    }
    
    @Override
    protected void edit(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        
    }
    
    @Override
    protected void update(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        this.redirect(request.getContextPath() + "/customer", response);
    }
    
    @Override
    protected void delete(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        this.redirect(request.getContextPath() + "/customer", response);
    }
}
