package com.library.project.beans;

import com.library.project.contracts.JpaTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity(name="User")
@Table(name = "users")
public class User extends JpaTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="email")
    @NotBlank(message = "Email field is required")
    private String email;

    @Column(name="password")
    @NotBlank(message = "Password field is required")
    private String password;

    public User() {
    }
    
    public User(String email, String password) {
        this.password = password;
        this.email = email;
    }
    
    public User(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}