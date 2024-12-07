package com.library.project.factories;

import com.library.project.beans.Customer;
import com.library.project.classes.EmailAttribute;
import com.library.project.contracts.ModelValidator;
import com.library.project.exceptions.InvalidArgumentException;
import com.library.project.repositories.CustomerRepository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletRequest;

public class CustomerFactory extends ModelValidator {

    /**
     * @param request
     * @return
     * @throws InvalidArgumentException
     */
    public static Customer fromRequest(HttpServletRequest request) throws InvalidArgumentException
    {
        EmailAttribute email = new EmailAttribute((String) request.getParameter("email"));;
        
        Customer customer = new Customer(
            request.getParameter("cin"),
            request.getParameter("name"),
            email.getValue()
        );

        return validate(customer);
    }
    
    /**
     * @param request
     * @return
     * @throws InvalidArgumentException
     */
    public static Customer fromRequest(HttpServletRequest request, String itemId) throws InvalidArgumentException
    {
        EmailAttribute email = new EmailAttribute((String) request.getParameter("email"));;
        
        Customer customer = new Customer(
            (long) Integer.parseInt(itemId),
            request.getParameter("cin"),
            request.getParameter("name"),
            email.getValue()
        );

        return validate(customer);
    }

    /**
     * @param request
     * @param entityManagerFactory
     * @return
     */
    public static Customer fromDatabase(HttpServletRequest request, EntityManagerFactory entityManagerFactory) {

        try (CustomerRepository customerRepository = new CustomerRepository(entityManagerFactory)) {

            long customerId;

            try {
                customerId = (long) Integer.parseInt(request.getParameter("customer_id"));
            } catch (NumberFormatException e) {
                customerId = 0;
            }
            
            return customerId > 0 
                ? customerRepository.find(customerId)
                : customerRepository.findByCin(request.getParameter("cin"));
        }
    }
}
