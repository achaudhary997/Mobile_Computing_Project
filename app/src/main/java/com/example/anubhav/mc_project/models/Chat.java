package com.example.anubhav.mc_project.models;

import java.util.ArrayList;

public class Chat {
    private ArrayList<Message> messages;
    private String sender;
    private String receiver;

    public Chat() {
        messages = new ArrayList<>();
    }

    public Chat(String sender, String receiver) {
        messages = new ArrayList<>();
        this.sender = sender;
        this.receiver = receiver;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addMessages(Message message) {
        this.messages.add(message);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
