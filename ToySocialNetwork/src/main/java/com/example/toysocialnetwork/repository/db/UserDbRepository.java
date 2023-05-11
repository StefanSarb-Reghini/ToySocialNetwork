package com.example.toysocialnetwork.repository.db;

import com.example.toysocialnetwork.domain.User;
import com.example.toysocialnetwork.domain.validators.Validator;
import com.example.toysocialnetwork.exceptions.ArgumentException;
import com.example.toysocialnetwork.exceptions.RepositoryException;
import com.example.toysocialnetwork.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDbRepository implements Repository<Long, User> {
    private String url;
    private String usernm;
    private String pass;
    private Validator<User> validator;

    public UserDbRepository(String url, String usernm, String pass, Validator<User> validator) {
        this.url = url;
        this.usernm = usernm;
        this.pass = pass;
        this.validator = validator;
    }

    @Override
    public User findOne(Long id) {
        if (id == null) {
            throw new ArgumentException("The ID that has to be found cannot be null!\n");
        }

        String sql = "SELECT * from users where id = ?";
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                User user = new User(username, password, email);
                user.setId(id);
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                User user = new User(username, password, email);
                user.setId(id);
                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new ArgumentException("Entity to be saved cannot be null!\n");
        }

        var allUsers = findAll();
        for (User user1 : allUsers)
            if (user.equals(user1)) {
                throw new RepositoryException("Username or email already taken!\n");
            }

        validator.validate(user);
        String sql = "insert into users (username, password, email) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());

            ps.executeUpdate();
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User delete(Long id) {
        if (id == null) {
            throw new ArgumentException("The ID to be deleted cannot be null");
        }

        User user = findOne(id);
        if (user == null) {
            throw new RepositoryException("Entity doesn't exist!");
        }

        String sql = "DELETE from users where id = ?";
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            statement.setLong(1, id);
            statement.executeUpdate();

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User update(User user) {
        if (user == null) {
            throw new ArgumentException("Entity to be updated cannot be null!\n");
        }

        if (findOne(user.getId()) == null) {
            throw new RepositoryException("Entity doesn't exist!");
        }

        validator.validate(user);
        String sql = "UPDATE users SET username = ?, password = ?, email = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setLong(4, user.getId());

            statement.executeUpdate();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User findByEmailPass(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (
                Connection connection = DriverManager.getConnection(url, usernm, pass);
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                User user = new User(username, password, email);
                user.setId(id);
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}