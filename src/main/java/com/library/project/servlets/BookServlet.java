package com.library.project.servlets;

import java.io.IOException;

import com.library.project.beans.Book;
import com.library.project.classes.JsonResponse;
import com.library.project.contracts.HttpBaseController;
import com.library.project.factories.BookFactory;
import com.library.project.repositories.BookRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/book/*", "/book"})
public class BookServlet extends HttpBaseController {
    
    /**
     * This controller will serve ajax requests only
     */
    @Override
    protected boolean ajaxOnly() {
        return true;
    }
    
    @Override
    protected void index(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {
        
        JsonResponse jsonResponse;
        
        try (BookRepository repository = new BookRepository(entityManagerFactory)) {
            jsonResponse = new JsonResponse(201, repository.paginate(request));
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
        
        try (BookRepository repository = new BookRepository(entityManagerFactory)) {
            Book book = BookFactory.fromRequest(request);
            repository.create(book);
            this.json(new JsonResponse(201, book), response);
        } catch (Exception e) {
            this.json(new JsonResponse(419, e.getMessage()), response);
        }
    }
    
    @Override
    protected void update(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {

        try (BookRepository repository = new BookRepository(entityManagerFactory)) {
            Book book = BookFactory.fromRequest(request, id);
            repository.updateBook(book);
            this.json(new JsonResponse(201, book), response);
        } catch (Exception e) {
            this.json(new JsonResponse(419, e.getMessage()), response);
        }
    }
    
    @Override
    protected void delete(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {

        try (BookRepository repository = new BookRepository(entityManagerFactory)) {
            Book book = BookFactory.fromRequest(request, id);
            repository.delete(book);
            this.json(new JsonResponse(201, "item deleted!", book), response);
        } catch (Exception e) {
            this.json(new JsonResponse(419, e.getMessage()), response);
        }
    }
}
