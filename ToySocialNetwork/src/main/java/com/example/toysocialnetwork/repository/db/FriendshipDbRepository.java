package com.example.toysocialnetwork.repository.db;

import com.example.toysocialnetwork.domain.Friendship;
import com.example.toysocialnetwork.domain.validators.Validator;
import com.example.toysocialnetwork.exceptions.ArgumentException;
import com.example.toysocialnetwork.exceptions.RepositoryException;
import com.example.toysocialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipDbRepository implements Repository<Long, Friendship> {
    private String url;
    private String usernm;
    private String pass;
    private Validator<Friendship> validator;

    public FriendshipDbRepository(String url, String usernm, String pass, Validator<Friendship> validator) {
        this.url = url;
        this.usernm = usernm;
        this.pass = pass;
        this.validator = validator;
    }

    @Override
    public Friendship findOne(Long id) {
        if (id == null) {
            throw new ArgumentException("The ID that has to be found cannot be null!\n");
        }

        String sql = "SELECT * from friendships where id = ?";
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id_user1");
                Long id2 = resultSet.getLong("id_user2");
                Date friendsFrom = resultSet.getDate("friends_from");
                Date requestDate = resultSet.getDate("request_date");
                LocalDate requestDateLocal = requestDate.toLocalDate();
                String status = resultSet.getString("status");
                Friendship friendship;
                if (friendsFrom != null) {
                    LocalDate friendsFromLocal = friendsFrom.toLocalDate();
                    friendship = new Friendship(id1, id2, requestDateLocal, friendsFromLocal, status);
                } else {
                    friendship = new Friendship(id1, id2, requestDateLocal, status);
                }
                friendship.setId(id);
                return friendship;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Friendship findOneBy2ids(Long id1, Long id2) {
        String sql = "SELECT * from friendships where (id_user1 = ? AND id_user2 = ?) OR (id_user1 = ? AND id_user2 = ?)";
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id1);
            ps.setLong(2, id2);
            ps.setLong(3, id2);
            ps.setLong(4, id1);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                id1 = resultSet.getLong("id_user1");
                id2 = resultSet.getLong("id_user2");
                Long id = resultSet.getLong("id");
                Date friendsFrom = resultSet.getDate("friends_from");
                Date requestDate = resultSet.getDate("request_date");
                LocalDate requestDateLocal = requestDate.toLocalDate();
                String status = resultSet.getString("status");
                Friendship friendship;
                if (friendsFrom != null) {
                    LocalDate friendsFromLocal = friendsFrom.toLocalDate();
                    friendship = new Friendship(id1, id2, requestDateLocal, friendsFromLocal, status);
                } else {
                    friendship = new Friendship(id1, id2, requestDateLocal, status);
                }
                friendship.setId(id);
                return friendship;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id1 = resultSet.getLong("id_user1");
                Long id2 = resultSet.getLong("id_user2");
                Date friendsFrom = resultSet.getDate("friends_from");
                Date requestDate = resultSet.getDate("request_date");
                LocalDate requestDateLocal = requestDate.toLocalDate();
                String status = resultSet.getString("status");
                Friendship friendship;
                if (friendsFrom != null) {
                    LocalDate friendsFromLocal = friendsFrom.toLocalDate();
                    friendship = new Friendship(id1, id2, requestDateLocal, friendsFromLocal, status);
                } else {
                    friendship = new Friendship(id1, id2, requestDateLocal, status);
                }
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Friendship save(Friendship friendship) {
        if (friendship == null) {
            throw new ArgumentException("Entity to be saved cannot be null!\n");
        }

        var allFriendships = findAll();
        for (Friendship friendship1 : allFriendships)
            if (friendship.equals(friendship1)) {
                throw new ArgumentException("Entity already exists!\n");
            }

        validator.validate(friendship);
        String sql = "insert into friendships (id_user1, id_user2, request_date, status) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, friendship.getId1());
            ps.setLong(2, friendship.getId2());
            ps.setDate(3, Date.valueOf(friendship.getRequestDate()));
            ps.setString(4, friendship.getStatus());

            ps.executeUpdate();
            return friendship;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Friendship delete(Long id) {
        if (id == null) {
            throw new ArgumentException("The ID to be deleted cannot be null");
        }

        Friendship friendship = findOne(id);
        if (friendship == null) {
            throw new RepositoryException("Entity doesn't exist!");
        }

        String sql = "DELETE from friendships where id = ?";
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            statement.setLong(1, id);
            statement.executeUpdate();

            return friendship;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Long> findAllSentRequestsUsers(Long id1) {

        List<Long> users = new ArrayList<>();
        String sql = "SELECT id_user2 FROM friendships WHERE id_user1 = ? AND status <> 'declined'";
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            statement.setLong(1, id1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(resultSet.getLong("id_user2"));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Long> findAllRequestsUsers(Long id2) {

        List<Long> users = new ArrayList<>();
        String sql = "SELECT id_user1 FROM friendships WHERE id_user2 = ?";
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            statement.setLong(1, id2);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(resultSet.getLong("id_user1"));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Long> getFriendsForUser(Long id) {

        List<Long> users = new ArrayList<>();
        String sql = "SELECT * FROM friendships WHERE (id_user1 = ? OR id_user2 = ?) AND status = 'Accepted'";
        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            statement.setLong(1, id);
            statement.setLong(2, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id_user1");
                Long id2 = resultSet.getLong("id_user2");
                if (id.equals(id1))
                    users.add(id2);
                else
                    users.add(id1);
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Friendship update(Friendship friendship) {
        if (friendship == null) {
            throw new ArgumentException("Entity to be updated cannot be null!\n");
        }

        if (findOne(friendship.getId()) == null) {
            throw new RepositoryException("Entity doesn't exist!");
        }

        validator.validate(friendship);
        String sql = "UPDATE friendships SET friends_from = ?, status = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, usernm, pass);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setDate(1, Date.valueOf(friendship.getFriendsFrom()));
            statement.setString(2, friendship.getStatus());
            statement.setLong(3, friendship.getId());

            statement.executeUpdate();
            return friendship;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
