package com.library.project.beans;

import com.library.project.contracts.JpaTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity(name="Customer")
@Table(name = "customers")
public class Customer extends JpaTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "CIN field is required")
    @Column(name="cin", unique=true, nullable=false)
    private String cin;

    @NotBlank(message = "Name is required")
    @Column(name="name")
    private String name;

    @NotBlank(message = "Email is required")
    @Column(name="email")
    private String email;

    public Customer() {}

    public Customer(String cin, String name, String email) {
        this.cin = cin;
        this.name = name;
        this.email = email;
    }

    public Customer(long id, String cin, String name, String email) {
        this.id = id;
        this.cin = cin;
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCin() {
        return this.cin.toUpperCase();
    }

    public void setCin(String cin) {
        this.cin = cin.toUpperCase();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}