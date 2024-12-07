package com.library.project.servlets;

import java.io.IOException;

import com.library.project.beans.Book;
import com.library.project.beans.Borrow;
import com.library.project.beans.Customer;
import com.library.project.classes.JsonResponse;
import com.library.project.contracts.HttpBaseController;
import com.library.project.factories.BookFactory;
import com.library.project.factories.BorrowFactory;
import com.library.project.factories.CustomerFactory;
import com.library.project.repositories.BorrowRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/borrow/*")
public class BorrowServlet extends HttpBaseController {
    
    @Override
    protected void index(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {

        if (! this.ajax(request)) {
            this.view("views/borrow.jsp", request, response);
        }

        try (BorrowRepository repository = new BorrowRepository(entityManagerFactory)) {;
            JsonResponse.send(response, new JsonResponse(201, repository.paginate(request)));
        } catch (Exception e) {
            JsonResponse.send(response, new JsonResponse(419, e.getMessage()));
        }
    }
    
    @Override
    protected void store(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {

        JsonResponse jsonResponse;
        
        try (BorrowRepository borrowRepository = new BorrowRepository(entityManagerFactory)) {
            Borrow borrow = BorrowFactory.fromRequest(request, entityManagerFactory);

            if (! borrowRepository.isBorrowable(borrow)) {
                throw new Exception("Item already borrowed and not returned yet!");
            }
            
            borrowRepository.create(borrow);
            
            jsonResponse = new JsonResponse(201, borrow);
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
        
        this.view("borrow/show", request, response);
    }
    
    @Override
    protected void update(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        JsonResponse jsonResponse;
        
        try (BorrowRepository borrowRepository = new BorrowRepository(entityManagerFactory)) {
            Customer customer = CustomerFactory.fromDatabase(request, entityManagerFactory);
            Book book = BookFactory.fromDatabase(request, entityManagerFactory);
            Borrow borrow = borrowRepository.findPendingByBookAndCustomer(customer, book);
            borrowRepository.markAsReturned(borrow);
            jsonResponse = new JsonResponse(201, borrow);
        } catch (Exception e) {
            jsonResponse = new JsonResponse(
                419, 
                e.getMessage().startsWith("No result found for query") 
                    ? "This customer didn't borrow this book!"
                    : e.getMessage()
            );
        }

        this.json(jsonResponse, response);
    }
}
