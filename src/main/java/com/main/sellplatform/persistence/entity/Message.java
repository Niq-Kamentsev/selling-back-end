package com.main.sellplatform.persistence.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

public class Message {
    private Long id;
    @NotNull
    private Long sender;
    @NotNull
    private Long receiver;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date dateTime;
    @NotEmpty
    @Size(max = 2048, message = "Incorrect message length")
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
		this.receiver = receiver;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
