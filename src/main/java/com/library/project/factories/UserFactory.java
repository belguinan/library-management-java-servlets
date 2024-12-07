package com.library.project.factories;

import com.library.project.beans.User;
import com.library.project.classes.EmailAttribute;
import com.library.project.classes.PasswordAttribute;
import com.library.project.contracts.ModelValidator;
import com.library.project.exceptions.InvalidArgumentException;
import com.library.project.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

public class UserFactory extends ModelValidator {

    /**
     * @param request
     * @return
     * @throws InvalidArgumentException
     */
    public static User fromRequest(HttpServletRequest request) throws InvalidArgumentException
    {
        // Get user password
        PasswordAttribute password = new PasswordAttribute((String) request.getParameter("password"));

        // Validate the email address
        EmailAttribute email = new EmailAttribute((String) request.getParameter("email"));

        // 
        User user;
        
        try {
            // Get user id
            long id = (long) Long.parseLong(request.getParameter("_id"));
            user = new User(id, email.getValue(), password.toMd5());
        } catch (NumberFormatException e) {
            user = new User(email.getValue(), password.toMd5());
        }

        return validate(user);
    }

    /**
     * @param request
     * @param repository
     * @return
     * @throws InvalidArgumentException
     */
    public static User fromLoginRequest(HttpServletRequest request, UserRepository repository) throws InvalidArgumentException
    {
        // Get user password
        PasswordAttribute password = new PasswordAttribute((String) request.getParameter("password"));

        // Validate the email address
        EmailAttribute email = new EmailAttribute((String) request.getParameter("email"));

        return repository.findByCredentials(email.getValue(), password.toMd5());
    }
}
