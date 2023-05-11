package com.example.toysocialnetwork.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long>{
    private String message;
    private Long idSender;
    private Long idReciever;
    private LocalDateTime sentDate;

    public Message(String message, Long idSender, Long idReciever, LocalDateTime sentDate) {
        this.message = message;
        this.idSender = idSender;
        this.idReciever = idReciever;
        this.sentDate = sentDate;
    }

    public String getMessage() {
        return message;
    }

    public Long getIdSender() {
        return idSender;
    }

    public Long getIdReciever() {
        return idReciever;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }
}
