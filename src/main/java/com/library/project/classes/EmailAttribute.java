package com.library.project.classes;

import com.library.project.contracts.Attribute;
import com.library.project.exceptions.InvalidArgumentException;

public final class EmailAttribute implements Attribute {

    /**
     * Valid email address
     */
    private String email;

    /**
     * @param String email
     */
    public EmailAttribute(String email) throws InvalidArgumentException
    {   
        if (! this.isValid(email)) {
            throw new InvalidArgumentException("Invalid email address");
        }

        this.email = email;
    }

    /**
     * @param email
     * @return Boolean
     */
    public Boolean isValid(String email)
    {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * @return String
     */
    public String getValue()
    {
        return this.email;
    }
}
