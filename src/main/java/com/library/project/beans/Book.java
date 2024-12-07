package com.library.project.beans;

import com.library.project.contracts.JpaTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity(name="Book")
@Table(name = "books")
public class Book extends JpaTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title is required")
    @Column(name="title")
    private String title;

    @NotBlank(message = "Author is required")
    @Column(name="author")
    private String author;

    @NotBlank(message = "Category is required")
    @Column(name="category")
    private String category;

    @Min(value=1, message = "Quantity is required")
    @Column(name="quantity")
    private int quantity;

    @Column(name="available_quantity")
    private int availableQuantity;

    @Column(name="description")
    private String description;

    public Book() {}

    public Book(String title, String author, int quantity, String category, String description) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.availableQuantity = quantity;
        this.category = category;
        this.description = description;
    }
    
    public Book(long id, String title, String author, int quantity, String category, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.availableQuantity = quantity;
        this.category = category;
        this.description = description;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void decrimentAvailableQuantity() {
        this.availableQuantity--;
    }

    public void incrementAvailableQuantity() {
        this.availableQuantity++;
    }

    public int getAvailableQuantity() {
        return this.availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}