package com.example.toysocialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {
    private String username;
    private String password;
    private String email;

    private List<User> friends;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User user) {
        this.friends.add(user);
    }

    public void deleteFriend(User user) {
        this.friends.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername()) && getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getEmail());
    }

    @Override
    public String toString() {
        int noOfFriends;
        if (friends == null) {
            noOfFriends = 0;
        } else {
            noOfFriends = friends.size();
        }

        return "Id: " + getId() + ", Username: " + getUsername() + ", Email: " + getEmail() + ", Friends: " + noOfFriends;
    }

    public String toFileFormat() {
        return getId().toString() + ',' + getUsername() + ',' + getPassword() + ',' + getEmail();
    }
}
