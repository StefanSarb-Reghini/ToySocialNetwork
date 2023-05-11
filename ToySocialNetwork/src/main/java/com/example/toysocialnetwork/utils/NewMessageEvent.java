package com.example.toysocialnetwork.utils;

import com.example.toysocialnetwork.domain.Message;

public class NewMessageEvent implements Event{
    private Message message;

    public NewMessageEvent(Message message){
        this.message = message;
    }

    public Message getMessage(){
        return message;
    }
}
