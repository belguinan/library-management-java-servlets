package com.library.project.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.library.project.beans.Book;
import com.library.project.classes.PaginationResult;

import jakarta.persistence.Query;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletRequest;

public class BookRepository extends DatabaseRepository<Book> {

    /**
     * @param entityManagerFactory
     */
    public BookRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    /**
     * @param book
     * @throws Exception
     */
    public void updateBook(Book book) throws Exception
    {
        Book oldBook = super.find(Book.class, book.getId());
        
        if (oldBook.getAvailableQuantity() > book.getQuantity()) {
            throw new Exception("Quantity must be more than available quantity (" + String.valueOf(oldBook.getAvailableQuantity()) + ")");
        }

        int newAvailableQuantity = oldBook.getAvailableQuantity() + ( book.getQuantity() - oldBook.getQuantity() );
        
        book.setAvailableQuantity(newAvailableQuantity);
        
        super.update(book);
    }

    /**
     * @param model
     * @param page
     * @param perPage
     * @return
     */
    @SuppressWarnings("unchecked")
    public PaginationResult<Book> paginate(HttpServletRequest request) {

        int page = this.getIntRequestParam(request, "page", 1);

        int perPage = this.getIntRequestParam(request, "perPage", 25);

        List<Book> items = this.getSelectQuery("SELECT b FROM Book b", Book.class, request)
            .setFirstResult((page - 1) * perPage)
            .setMaxResults(perPage)
            .getResultList();

        long totalItems = (long) this.getSelectQuery("SELECT COUNT(b) FROM Book b", Long.class, request)
            .getSingleResult();
    
        return new PaginationResult<>(items, page, perPage, totalItems);
    }

    /**
     * @param stockFilters
     * @return String
     */
    private String getAvailableQuantityCondition(String[] stockFilters) {

        if (stockFilters == null) {
            return null;
        }
        
        List<String> strockCondition = new ArrayList<>();
        
        for (String filter : stockFilters) {
            if (filter.equals("in-stock")) {
                strockCondition.add("(b.availableQuantity > 3)");
                continue;
            }

            if (filter.equals("low-stock")) {
                strockCondition.add("(b.availableQuantity BETWEEN 1 AND 3)");
                continue;
            }

            if (filter.equals("out-of-stock")) {
                strockCondition.add("(b.availableQuantity <= 0)");
            }
        }

        return !strockCondition.isEmpty() 
            ? String.join(" OR ", strockCondition) 
            : null;
    }

    /**
     * @param search
     * @return String
     */
    private String getSearchCondition(String search) {
        return search != null && !search.isEmpty() 
            ? "( CAST(b.id AS string) LIKE :search OR b.category LIKE :search OR b.title LIKE :search OR b.author LIKE :search OR b.description LIKE :search )" 
            : null;
    }

    /**
     * @param selectQuery
     * @param conditions
     * @param searchCondition
     * @param search
     * @return Query
     */
    private <T> Query getSelectQuery(String selectQuery, Class<T> resultType, HttpServletRequest request)
    {
        List<String> conditions = new ArrayList<>();
        
        String searchCondition = this.getSearchCondition(request.getParameter("search"));

        String stockCondition = this.getAvailableQuantityCondition(request.getParameterValues("stock[]"));

        if (searchCondition != null) {
            conditions.add(searchCondition);
        }

        if (stockCondition != null) {
            conditions.add(String.join(" OR ", stockCondition));
        }
        
        String queryString = !conditions.isEmpty() ? " WHERE " + String.join(" AND ", conditions) : "";

        queryString += " ORDER BY b.id DESC";
        
        Query query = this.entityManager.createQuery(selectQuery + queryString, resultType);

        if (searchCondition != null) {
            query.setParameter("search", "%" + request.getParameter("search") + "%");
        }


        return query;
    }    
}
