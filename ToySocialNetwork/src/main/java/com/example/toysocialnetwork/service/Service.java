package com.example.toysocialnetwork.service;

import com.example.toysocialnetwork.domain.Friendship;
import com.example.toysocialnetwork.domain.Message;
import com.example.toysocialnetwork.domain.User;
import com.example.toysocialnetwork.domain.validators.ValidationException;
import com.example.toysocialnetwork.exceptions.ArgumentException;
import com.example.toysocialnetwork.exceptions.RepositoryException;
import com.example.toysocialnetwork.observer.Observer;
import com.example.toysocialnetwork.repository.Repository;
import com.example.toysocialnetwork.repository.db.FriendshipDbRepository;
import com.example.toysocialnetwork.repository.db.MessageDbRepository;
import com.example.toysocialnetwork.repository.db.UserDbRepository;
import com.example.toysocialnetwork.utils.Event;
import com.example.toysocialnetwork.utils.Graph;
import com.example.toysocialnetwork.utils.NewMessageEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Service {
    private UserDbRepository userRepository;
    private FriendshipDbRepository friendshipRepository;

    private MessageDbRepository messageDbRepository;

    public Service(UserDbRepository userRepository, FriendshipDbRepository friendshipRepository, MessageDbRepository messageDbRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.messageDbRepository = messageDbRepository;
    }

    public List<Message> getAllForChat(Long idSender, Long idReciever){
        return messageDbRepository.getAll(idSender, idReciever);
    }

    public Message sendMessage(String msg, Long idSender, Long idReciever, LocalDateTime sentDate){
        Message message = new Message(msg, idSender, idReciever, sentDate);
        return messageDbRepository.save(message);
    }

    public void addObserver(Observer<NewMessageEvent> observer){
        messageDbRepository.addObserver(observer);
    }

    //finders
    public User findUser(Long id) {
        return userRepository.findOne(id);
    }

    public Friendship findFriendship(Long id) {
        return friendshipRepository.findOne(id);
    }

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Iterable<Friendship> findAllFriendships() {
        return friendshipRepository.findAll();
    }

    public User findUserByEmailPassword(String email, String password) {
        return userRepository.findByEmailPass(email, password);
    }

    /**
     * Adds a new user
     *
     * @param username = name of the user
     * @param password = password of the user
     * @param email    = email of the user
     * @throws ValidationException ;
     * @throws ArgumentException   ;
     * @throws RepositoryException ;
     */
    public void addUser(String username, String password, String email) throws ValidationException, ArgumentException, RepositoryException {
        User user = new User(username, password, email);
        userRepository.save(user);
    }

    /**
     * Deletes user with a specified id
     *
     * @param id = specified id
     * @throws ArgumentException   ;
     * @throws RepositoryException ;
     */
    public void deleteUser(Long id) throws ArgumentException, RepositoryException {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new RepositoryException("User cannot be found!");
        }

        List<Long> idsToBeDeleted = new ArrayList<>();

        for (Friendship friendship : friendshipRepository.findAll()) {
            if (Objects.equals(friendship.getId1(), user.getId()) || Objects.equals(friendship.getId2(), user.getId())) {
                idsToBeDeleted.add(friendship.getId());
            }
        }

        idsToBeDeleted.forEach(ID -> friendshipRepository.delete(ID));

        userRepository.delete(id);
    }


    public void sendRequest(Long id1, Long id2) {
        User user1 = findUser(id1);
        User user2 = findUser(id2);
        if (user1 == null || user2 == null) {
            throw new ValidationException("At least one of the users cannot be found!");
        }
        Friendship friendship = new Friendship(id1, id2);
        friendshipRepository.save(friendship); //throws repository exception if users are already friends
    }


    public void deleteFriend(Long id1, Long id2) {
        Friendship friendshipToDelete = friendshipRepository.findOneBy2ids(id1, id2);
        friendshipRepository.delete(friendshipToDelete.getId());
    }

    public void updateUser(Long id, String field, String newValue) {
        User user = userRepository.findOne(id);
        if (user != null) {
            if (field.equals("username")) {
                user.setUsername(newValue);
            }
            if (field.equals("password")) {
                user.setPassword(newValue);
            }
            if (field.equals("email")) {
                user.setEmail(newValue);
            }
        }
        userRepository.update(user);
    }

    public void updateFriendship(Long id, LocalDate newDate, String status) {
        Friendship friendship = findFriendship(id);
        if (friendship != null) {
            friendship.setFriendsFrom(newDate);
            friendship.setStatus(status);
        }
        friendshipRepository.update(friendship);
    }

    public String getFriendsInCommon(Long id1, Long id2) {
        List<Long> friendsOfUser1 = friendshipRepository.getFriendsForUser(id1);
        List<Long> friendsOfUser2 = friendshipRepository.getFriendsForUser(id2);
        Set<Long> friendsInCommon = friendsOfUser1.stream().distinct().filter(friendsOfUser2::contains).collect(Collectors.toSet());
        return String.valueOf(friendsInCommon.size());
    }

    public String getFriendsFrom(Long id1, Long id2) {
        Friendship friendship = friendshipRepository.findOneBy2ids(id1, id2);
        return friendship.getFriendsFrom().toString();
    }

    public String getRequestDate(Long id1, Long id2){
        Friendship friendship = friendshipRepository.findOneBy2ids(id1, id2);
        return friendship.getRequestDate().toString();
    }

    public List<Long> getAllFriendsForUser(Long id){
        return friendshipRepository.getFriendsForUser(id);
    }

    public Friendship findFriendshipBy2ids(Long id1, Long id2){
        return friendshipRepository.findOneBy2ids(id1, id2);
    }

    public String getFriendshipStatus(Long id1, Long id2){
        Friendship friendship = friendshipRepository.findOneBy2ids(id1, id2);
        if (friendship != null){
            return friendship.getStatus();
        }
        return "Not friends";
    }

    public List<Long> getSentRequestUsers(Long id){
        return friendshipRepository.findAllSentRequestsUsers(id);
    }

    public List<Long> getRequestUsers(Long id){
        return friendshipRepository.findAllRequestsUsers(id);
    }

    public int getAllCommunities(List<List<User>> list) {
        int res = 0;
        Map<Long, Boolean> visited = new HashMap<>();
        for (User user : userRepository.findAll()) {
            if (visited.get(user.getId()) == null) {
                List<User> community = new ArrayList<>();
                Graph.dfs(user, visited, community);
                list.add(community);
                res++;
            }
        }
        return res;
    }

    public List<User> mostSociableCommunity(List<List<User>> communities) {
        int resVal = 0;
        List<User> resList = new ArrayList<>();
        for (List<User> community : communities) {
            int resInt = 0;
            for (User user : community) {
                resInt = Integer.max(resInt, Graph.bfs(community, user));
            }

            if (resInt > resVal) {
                resVal = resInt;
                resList = community;
            }
        }
        return resList;
    }

}
