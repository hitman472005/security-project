package com.example.backend_security.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 512, nullable = false)
    private String token;

    @Column(name = "expiration_date", nullable = false)
    private Timestamp expirationDate;

    @Column(length = 512, nullable = false)
    private String valid;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp creationDate;

    public Token() {}

    public Token(Long id, User user, String token, Timestamp expirationDate, String valid, Timestamp creationDate) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expirationDate = expirationDate;
        this.valid = valid;
        this.creationDate = creationDate;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Timestamp getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Timestamp expirationDate) { this.expirationDate = expirationDate; }

    public String getValid() { return valid; }
    public void setValid(String valid) { this.valid = valid; }

    public Timestamp getCreationDate() { return creationDate; }
    public void setCreationDate(Timestamp creationDate) { this.creationDate = creationDate; }
}
