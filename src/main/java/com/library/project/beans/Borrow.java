package com.library.project.beans;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.library.project.contracts.JpaTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "borrows")
public class Borrow extends JpaTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Customer is required")
    @ManyToOne(cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @NotNull(message = "Book is required")
    @ManyToOne(cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "book_id", nullable = true)
    private Book book;

    @NotNull(message = "Expected return date is required")
    @Column(name = "expected_return_at", nullable = false)
    private Date expectedReturnAt;

    @Column(name = "returned_at")
    private Timestamp returnedAt;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getExpectedReturnAt() {
        return this.expectedReturnAt;
    }

    public void setExpectedReturnAt(Date expectedReturnAt) {
        this.expectedReturnAt = expectedReturnAt;
    }

    public Timestamp getReturnedAt() {
        return this.returnedAt;
    }

    public void setReturnedAt(Timestamp returnedAt) {
        this.returnedAt = returnedAt;
    }
}
