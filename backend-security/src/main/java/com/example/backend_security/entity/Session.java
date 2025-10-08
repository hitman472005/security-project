package com.example.backend_security.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 100)
    private String ip;

    @Column(columnDefinition = "TEXT")
    private String userAgent;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp start;

    private Timestamp end;

    @Column(length = 512, nullable = false)
    private String active;

    public Session() {}

    public Session(Long id, User user, String ip, String userAgent, Timestamp start, Timestamp end, String active) {
        this.id = id;
        this.user = user;
        this.ip = ip;
        this.userAgent = userAgent;
        this.start = start;
        this.end = end;
        this.active = active;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public Timestamp getStart() { return start; }
    public void setStart(Timestamp start) { this.start = start; }

    public Timestamp getEnd() { return end; }
    public void setEnd(Timestamp end) { this.end = end; }

    public String getActive() { return active; }
    public void setActive(String active) { this.active = active; }
}