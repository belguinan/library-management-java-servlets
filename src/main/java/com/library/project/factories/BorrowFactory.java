package com.library.project.factories;

import java.sql.Date;

import com.library.project.beans.Book;
import com.library.project.beans.Borrow;
import com.library.project.beans.Customer;
import com.library.project.contracts.ModelValidator;
import com.library.project.repositories.BookRepository;
import com.library.project.repositories.BorrowRepository;
import com.library.project.repositories.CustomerRepository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletRequest;

public class BorrowFactory extends ModelValidator {
    
    /**
     * @param request
     * @param entityManagerFactory
     * @return Borrow
     */
    public static Borrow fromRequest(HttpServletRequest request, EntityManagerFactory entityManagerFactory) {

        Borrow borrow = new Borrow();

        try (BookRepository bookRepository = new BookRepository(entityManagerFactory)) {
            borrow.setBook(bookRepository.find(
                Book.class, 
                (long) Integer.parseInt(request.getParameter("book_id")))
            );
        }
        
        try (CustomerRepository customerRepository = new CustomerRepository(entityManagerFactory)) {

            long customerId;

            try {
                customerId = (long) Integer.parseInt(request.getParameter("customer_id"));
            } catch (NumberFormatException e) {
                customerId = 0;
            }
            
            Customer customer = customerId > 0 
                ? customerRepository.find(customerId)
                : customerRepository.findByCin(request.getParameter("cin"));

            borrow.setCustomer(customer);
        }

        if (request.getParameter("expected_return_at") != null) {
            borrow.setExpectedReturnAt(Date.valueOf(request.getParameter("expected_return_at")));
        }
        
        return validate(borrow);
    }

}
