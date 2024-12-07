package com.library.project.repositories;

import com.library.project.beans.User;

import jakarta.persistence.EntityManagerFactory;

public class UserRepository extends DatabaseRepository<User> {

    /**
     * @param entityManagerFactory
     */
    public UserRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    /**
     * @param email
     * @param password
     * @return
     */
    public User findByCredentials(String email, String password) {
        return this.entityManager
            .createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
            .setParameter("email", email)
            .setParameter("password", password)
            .getSingleResult();
    }
}
