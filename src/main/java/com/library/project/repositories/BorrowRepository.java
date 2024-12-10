package com.library.project.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.library.project.beans.Book;
import com.library.project.beans.Borrow;
import com.library.project.beans.Customer;
import com.library.project.classes.PaginationResult;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;

public class BorrowRepository extends DatabaseRepository<Borrow> {

    /**
     * @param entityManagerFactory
     */
    public BorrowRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    /**
     * @param borrow
     * @return
     * @throws Exception
     */
    @Override
    public void create(Borrow borrow) throws Exception {

        if (borrow.getBook().getAvailableQuantity() == 0) {
            throw new Exception("Book is not borrowable, out of stock!");
        }
        
        if (! this.isBorrowable(borrow)) {
            throw new Exception("Book not returned yet!");
        }
        
        try {
            this.entityManager.getTransaction().begin();

            Book book = this.entityManager.merge(borrow.getBook());
            book.decrimentAvailableQuantity();

            this.entityManager.persist(borrow);
            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (this.entityManager.getTransaction().isActive()) {
                this.entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    /**
     * @param customer
     * @param book
     */
    public Borrow findPendingByBookAndCustomer(Customer customer, Book book)
    {
        Borrow borrow = this.entityManager
                .createQuery("SELECT b FROM Borrow b WHERE b.book = :book AND b.customer = :customer AND b.returnedAt IS NULL", Borrow.class)
                .setParameter("book", book)
                .setParameter("customer", customer)
                .setMaxResults(1)
                .getSingleResult();

        borrow.setCustomer(customer);
        borrow.setBook(book);
                
        return borrow;
    }
    
    /**
     * @param borrow
     * @throws Exception
     */
    public void markAsReturned(Borrow borrow) throws Exception {
        
        Book book = borrow.getBook();

        if (book.getAvailableQuantity() >= book.getQuantity()) {
            throw new Exception("Book already returned!");
        }

        try {
            this.entityManager.getTransaction().begin();

            book = this.entityManager.merge(book);
            book.incrementAvailableQuantity();
            
            borrow = this.entityManager.merge(borrow);
            borrow.setReturnedAt(Timestamp.from(Instant.now()));

            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (this.entityManager.getTransaction().isActive()) {
                this.entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }
    
    /**
     * @param borrow
     * @return boolean
     * @throws Exception
     */
    public boolean isBorrowable(Borrow borrow) throws Exception {
        try {
            String query = """
            SELECT b FROM Borrow b WHERE b.book = :book AND b.customer = :customer AND b.returnedAt IS NULL
            """;
            
            this.entityManager
                .createQuery(query, Borrow.class)
                .setParameter("book", borrow.getBook())
                .setParameter("customer", borrow.getCustomer())
                .setMaxResults(1)
                .getSingleResult();
            return false;
        } catch (NoResultException e) {
            return true;
        }
    }

    /**
     * @param page
     * @param perPage
     * @return List<Borrow>
     */
    public <T> Query getBorrowingHistoryQuery(String query, Class<T> model, HttpServletRequest request)
    {   
        List<String> queryCondition = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();

        int bookId = this.getIntRequestParam(request, "book_id", 0);

        if (bookId > 0) {
            queryCondition.add("( CAST(b.book.id as STRING) = :bookId )");
            params.put("bookId", String.valueOf(bookId));
        }

        if (request.getParameter("search") != null && !request.getParameter("search").isEmpty()) {
            queryCondition.add("( CAST(b.book.id as STRING) LIKE :search OR b.customer.cin LIKE :search OR b.book.title LIKE :search OR b.customer.name LIKE :search )");
            params.put("search", "%"+ request.getParameter("search") +"%");
        }

        if (! queryCondition.isEmpty()) {
            query += " WHERE " + String.join(" OR ", queryCondition);
        }

        query += " ORDER BY b.expectedReturnAt ASC, b.id DESC";
        
        System.err.println(query);
        
        Query qResult = this.entityManager.createQuery(query, model);

        params.forEach((key, value) -> {
            qResult.setParameter(key, value);
        });
        
        return qResult;
    }

    /**
     * @param model
     * @param page
     * @param perPage
     * @return
     */
    @SuppressWarnings("unchecked")
    public PaginationResult<Borrow> paginate(HttpServletRequest request) {

        int page = this.getIntRequestParam(request, "page", 1);

        int perPage = this.getIntRequestParam(request, "perPage", 25);

        long totalItems = (long) this.getBorrowingHistoryQuery("SELECT COUNT(b) FROM Borrow b", Long.class, request)
            .getSingleResult();

        List<Borrow> items = this.getBorrowingHistoryQuery("SELECT DISTINCT b FROM Borrow b", Borrow.class, request)
            .setFirstResult((page - 1) * perPage)
            .setMaxResults(perPage)
            .getResultList();

        return new PaginationResult<>(items, page, perPage, totalItems);
    }
}
