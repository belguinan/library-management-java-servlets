package com.library.project.contracts;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public interface JpaEntityManager {

    public static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");   

    public default void destroy() {
        try {
            entityManagerFactory.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
