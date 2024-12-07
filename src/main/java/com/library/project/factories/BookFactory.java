package com.library.project.factories;

import com.library.project.beans.Book;
import com.library.project.contracts.ModelValidator;
import com.library.project.exceptions.InvalidArgumentException;
import com.library.project.repositories.BookRepository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletRequest;

public class BookFactory extends ModelValidator {

    /**
     * @param request
     * @return
     * @throws InvalidArgumentException
     */
    public static Book fromRequest(HttpServletRequest request)
    {
        Book book = new Book(
            (String) request.getParameter("title"), 
            (String) request.getParameter("author"), 
            (int) Integer.parseInt((String) request.getParameter("quantity")),
            (String) request.getParameter("category"),
            (String) request.getParameter("description")
        );

        if (request.getParameter("id") != null) {
            long id = (long) Integer.parseInt(request.getParameter("id"));
            book.setId(id);
        }

        return validate(book);
    }
    
    /**
     * @param request
     * @return
     * @throws InvalidArgumentException
     */
    public static Book fromRequest(HttpServletRequest request, String itemId)
    {
        Book book = new Book(
            (String) request.getParameter("title"), 
            (String) request.getParameter("author"), 
            (int) Integer.parseInt((String) request.getParameter("quantity")),
            (String) request.getParameter("category"),
            (String) request.getParameter("description")
        );

        long id = (long) Integer.parseInt(itemId);
        book.setId(id);

        return validate(book);
    }

    /**
     * @param request
     * @param entitymanagerfactory
     * @return
     */
    public static Book fromDatabase(HttpServletRequest request, EntityManagerFactory entitymanagerfactory) {
        try (BookRepository bookRepository = new BookRepository(entitymanagerfactory)) {
            try {
                return bookRepository.find(
                    Book.class, 
                    (long) Integer.parseInt(request.getParameter("book_id"))
                );
            } catch (Exception e) {
                return bookRepository.find(
                    Book.class, 
                    (long) Integer.parseInt(request.getParameter("id"))
                );
            }
        }
    }
}
