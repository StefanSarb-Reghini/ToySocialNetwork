package com.example.toysocialnetwork.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private Long id1;
    private Long id2;

    private LocalDate friendsFrom;

    private LocalDate requestDate;

    private String status;

    public Friendship(Long id1, Long id2, LocalDate requestDate, LocalDate friendsFrom, String status) {
        this.id1 = id1;
        this.id2 = id2;
        this.requestDate = requestDate;
        this.friendsFrom = friendsFrom;
        this.status = status;
    }

    public Friendship(Long id1, Long id2) {
        this.id1 = id1;
        this.id2 = id2;
        this.requestDate = LocalDate.now();
        this.friendsFrom = null;
        this.status = "Pending";
    }

    public Friendship(Long id1, Long id2, LocalDate requestDate, String status){
        this.id1 = id1;
        this.id2 = id2;
        this.requestDate = requestDate;
        this.friendsFrom = null;
        this.status = status;
    }

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }

    public LocalDate getFriendsFrom() {
        return friendsFrom;
    }


    public String getStatus() {
        return status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFriendsFrom(LocalDate friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship that)) return false;
        return Objects.equals(getId1(), that.getId1()) && Objects.equals(getId2(), that.getId2()) || Objects.equals(getId1(), that.getId2()) && Objects.equals(getId2(), that.getId1());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId1(), getId2());
    }

    @Override
    public String toString() {
        return "ID: " + getId().toString() + ", Friendship: " + id1.toString() + " and " + id2.toString() + " since " + friendsFrom.toString();
    }

    public String toFileFormat() {
        return getId().toString() + ',' + id1.toString() + ',' + id2.toString() + ',' + friendsFrom.toString();
    }
}
