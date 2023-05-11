package com.example.toysocialnetwork.gui;

import com.example.toysocialnetwork.domain.Message;
import com.example.toysocialnetwork.domain.MessageDTO;
import com.example.toysocialnetwork.domain.User;
import com.example.toysocialnetwork.observer.Observer;
import com.example.toysocialnetwork.service.Service;
import com.example.toysocialnetwork.utils.NewMessageEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Chat implements Observer<NewMessageEvent> {
    private ObservableList<MessageDTO> modelMessage = FXCollections.observableArrayList();

    private Service service;

    private User sender;
    private User reciever;

    @FXML
    private TableView<MessageDTO> tableView;

    @FXML
    private TableColumn<String, MessageDTO> messageColumn;

    @FXML
    private TextField messageBar;

    @FXML
    private Button sendBtn;

    public void setChat(Service service, User sender, User reciever) {
        this.service = service;
        this.sender = sender;
        this.reciever = reciever;
        this.service.addObserver(this);
        messageColumn.setText(reciever.getUsername());
        modelMessage.setAll(getMessagesDTO());
    }

    @FXML
    private void initialize() {
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        tableView.setItems(modelMessage);
    }

    private void refresh(){
        modelMessage.setAll(getMessagesDTO());
        tableView.setItems(modelMessage);
    }

    private String formMessage(Message message) {
        String prefix;
        String dateTime = message.getSentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        if (message.getIdSender().equals(sender.getId()))
            prefix = "Me (" + dateTime + "): ";
        else {
            prefix = reciever.getUsername() + " (" + dateTime + "): ";
        }

        String finalMessage = prefix + message.getMessage();

        if(finalMessage.length() > 60) {
            return finalMessage.replaceAll("(.{60})", "$1\n");
        }

        return finalMessage;
    }

    private List<MessageDTO> getMessagesDTO() {
        List<Message> messages = service.getAllForChat(sender.getId(), reciever.getId());

        return messages.stream().map(x -> new MessageDTO(x.getId(), formMessage(x), x.getSentDate())).collect(Collectors.toList());
    }

    @FXML
    public void sendBtnClicked(){

        if(messageBar.getText().isEmpty())
            return;

        service.sendMessage(messageBar.getText(), sender.getId(), reciever.getId(), LocalDateTime.now());
        modelMessage.setAll(getMessagesDTO());
        messageBar.clear();
    }

    @Override
    public void update(NewMessageEvent event) {
        refresh();
    }
}
