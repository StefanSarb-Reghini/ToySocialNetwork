package com.example.toysocialnetwork.domain;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private String message;
    private LocalDateTime sentDate;

    public MessageDTO(Long id, String message, LocalDateTime sentDate) {
        this.id = id;
        this.message = message;
        this.sentDate = sentDate;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }
}
