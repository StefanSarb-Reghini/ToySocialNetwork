package com.example.toysocialnetwork.domain;

public class UserDTO {

    private Long id;
    private String username;
    private String friendsInCommon;
    private String since;
    private String status;

    public UserDTO(Long id, String username, String friendsInCommon, String since, String status) {
        this.id = id;
        this.username = username;
        this.friendsInCommon = friendsInCommon;
        this.since = since;
        this.status = status;
    }

    public UserDTO(Long id, String username, String friendsInCommon, String status) {
        this.id = id;
        this.username = username;
        this.friendsInCommon = friendsInCommon;
        this.since = null;
        this.status = status;
    }

    public UserDTO(Long id, String username, String friendsInCommon) {
        this.id = id;
        this.username = username;
        this.friendsInCommon = friendsInCommon;
        this.since = null;
        this.status = null;
    }

    public String getUsername() {
        return username;
    }


    public String getFriendsInCommon() {
        return friendsInCommon;
    }


    public String getSince() {
        return since;
    }


    public String getStatus() {
        return status;
    }

    public Long getId() { return id; }
}
