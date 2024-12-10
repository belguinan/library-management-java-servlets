package com.library.project.repositories;

import jakarta.persistence.Table;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import com.library.project.beans.Book;
import com.library.project.classes.PaginationResult;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class DatabaseRepository<T> implements AutoCloseable {

    /**
     * EntityManager must be created per request
     */
    protected EntityManager entityManager;
    
    public DatabaseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * @return EntityManager
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Select every single item in our table and 
     * Return it in a list without a pagination
     * 
     * @param model
     * @return
     */
    public List<T> get(Class<T> model) {
        return this.entityManager.createQuery("Select a from " + model.getSimpleName() + " a", model).getResultList();
    }
    
    /**
     * Select every single item in our table and 
     * Return it in a list without a pagination
     * 
     * @param model
     * @return
     */
    public T find(Class<T> model, long id) {
        return this.entityManager.find(model, id);
    }

    /**
     * Persiste the model to our entity manager persistence 
     * Context then we store it in our current database
     * 
     * @param model
     * @throws Exception
     */
    public void create(T model) throws Exception
    {
        this.inTransaction(() -> {
            this.entityManager.persist(model);
        });
    }
    
    /**
     * We must attach the model to the persistance 
     * Context in order to call remove directly
     * 
     * @param model
     */
    public void delete(T model)
    {
        this.inTransaction(() -> {
            this.entityManager.remove(
                this.entityManager.contains(model) 
                    ? model 
                    : this.entityManager.merge(model)
            );
        });
    }
    
    /**
     * We must attach the model to the persistance 
     * Context in order to call remove directly
     * 
     * @param model
     */
    public void update(T model)
    {
        this.inTransaction(() -> {
            this.entityManager.merge(model);
        });
    }
    
    /**
     * This is stupid probably, we use native sql query, 
     * must look for a better way here!
     * 
     * @param model
     */
    public void truncate(Class<T> model)
    {
        this.inTransaction(() -> {
            String query = "Truncate " + model.getAnnotation(Table.class).name();
            this.entityManager.createNativeQuery(query).executeUpdate();
        });
    }

    /**
     * First class function like implemented to prevent 
     * Code repetition probably there is a better way
     * 
     * @param callback
     */
    public void inTransaction(Runnable callback) {
        try {
            this.entityManager.getTransaction().begin();
            callback.run();
            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (this.entityManager.getTransaction().isActive()) {
                this.entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    /**
     * AutoClosable implementation must use this method, we should 
     * Close the entity manager with try with resource management
     */
    @Override
    public void close() {
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    /**
     * @param model
     * @return
     */
    public long count(Class<T> model)
    {
        return this.entityManager
            .createQuery("SELECT COUNT(a) FROM " + model.getSimpleName() + " a", Long.class)
            .getSingleResult();
    }
    
    /**
     * @param model
     * @param page
     * @param perPage
     * @return
     */
    public PaginationResult<T> paginate(Class<T> model, int page, int perPage) {

        long totalItems = this.count(model);
    
        List<T> items = this.entityManager
            .createQuery("SELECT a FROM " + model.getSimpleName() + " a", model)
            .setFirstResult((page - 1) * perPage)
            .setMaxResults(perPage)
            .getResultList();
    
        return new PaginationResult<>(items, page, perPage, totalItems);
    }

    /**
     * @param model
     * @param page
     * @param perPage
     * @return
     */
    public PaginationResult<T> paginate(HttpServletRequest request, Class<T> model) {

        int page = this.getIntRequestParam(request, "page", 1);

        int perPage = this.getIntRequestParam(request, "perPage", 25);

        return this.paginate(model, page, perPage);
    }

    /**
     * @param request
     * @param name
     * @param value
     * @return int
     */
    public int getIntRequestParam(HttpServletRequest request, String name, int value) {
        try {
            return Integer.parseInt(request.getParameter(name));
        } catch (Exception e) {
            return value;
        }
    }
}
