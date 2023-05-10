package com.gpse.sesam.domain.imprint;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Imprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    protected Imprint() {

    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Imprint(String content, LocalDateTime timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }


}
