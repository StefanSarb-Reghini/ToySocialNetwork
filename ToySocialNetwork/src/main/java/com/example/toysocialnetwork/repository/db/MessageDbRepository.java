package com.example.toysocialnetwork.repository.db;

import com.example.toysocialnetwork.domain.Message;
import com.example.toysocialnetwork.domain.User;
import com.example.toysocialnetwork.domain.validators.ValidationException;
import com.example.toysocialnetwork.domain.validators.Validator;
import com.example.toysocialnetwork.exceptions.ArgumentException;
import com.example.toysocialnetwork.exceptions.RepositoryException;
import com.example.toysocialnetwork.observer.ConcreteObservable;
import com.example.toysocialnetwork.repository.Repository;
import com.example.toysocialnetwork.utils.NewMessageEvent;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDbRepository extends ConcreteObservable<NewMessageEvent> implements Repository<Long, Message> {
    private String url;
    private String usernm;
    private String pass;
    private Validator<Message> validator;

    public MessageDbRepository(String url, String usernm, String pass, Validator<Message> validator) {
        this.url = url;
        this.usernm = usernm;
        this.pass = pass;
        this.validator = validator;
    }

    public List<Message> getAll(Long idSender, Long idReciever){
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (id_sender = ? AND id_reciever = ?) OR (id_sender = ? AND id_reciever = ?) ORDER BY sent_date";

        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idSender);
            ps.setLong(2, idReciever);
            ps.setLong(3, idReciever);
            ps.setLong(4, idSender);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String msg = resultSet.getString("message");
                Long idS = resultSet.getLong("id_sender");
                Long idR = resultSet.getLong("id_reciever");
                Timestamp sentDateTimestamp = resultSet.getTimestamp("sent_date");
                LocalDateTime sentDate = sentDateTimestamp.toLocalDateTime();
                Message message = new Message(msg, idS, idR, sentDate);
                message.setId(id);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message findOne(Long aLong) throws ArgumentException {
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        return null;
    }

    @Override
    public Message save(Message message) {
        String sql = "INSERT INTO messages (message, id_sender, id_reciever, sent_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, message.getMessage());
            ps.setLong(2, message.getIdSender());
            ps.setLong(3, message.getIdReciever());
            ps.setTimestamp(4, Timestamp.valueOf(message.getSentDate()));

            ps.executeUpdate();
            notifyObservers(new NewMessageEvent(message));
            return message;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long aLong) throws ArgumentException, RepositoryException {
        return null;
    }

    @Override
    public Message update(Message entity) throws ArgumentException, RepositoryException {
        return null;
    }
}
