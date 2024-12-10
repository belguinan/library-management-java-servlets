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
        
        try (CustomerRepository repository = new CustomerRepository(entityManagerFactory)) {
            this.json(new JsonResponse(201, repository.paginate(request, Customer.class)), response);
        } catch (Exception e) {
            this.json(new JsonResponse(419, e.getMessage()), response);
        }
    }
    
    @Override
    protected void store(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {

        try (CustomerRepository repository = new CustomerRepository(entityManagerFactory)) {
            Customer customer = repository.findOrCreate(
                CustomerFactory.fromRequest(request)
            );
            
            this.json(new JsonResponse(201, customer), response);
        } catch (Exception e) {
            this.json(new JsonResponse(419, e.getMessage()), response);
        }
    }
}
