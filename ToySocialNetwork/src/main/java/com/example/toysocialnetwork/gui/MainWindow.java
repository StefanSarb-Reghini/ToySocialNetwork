package com.example.toysocialnetwork.gui;

import com.example.toysocialnetwork.domain.Friendship;
import com.example.toysocialnetwork.domain.User;
import com.example.toysocialnetwork.domain.UserDTO;
import com.example.toysocialnetwork.service.Service;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainWindow {
    private Service service;

    private User loggedUser;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    private TextField searchBar;

    @FXML
    private Label tableLabel;

    @FXML
    private Label welcomeText;

    @FXML
    private Hyperlink logOut;

    @FXML
    private Button friendsBtn;

    @FXML
    private Button requestsBtn;

    @FXML
    private Button discoverNewBtn;

    @FXML
    private Button sentRequests;

    @FXML
    private Button addBtn;

    @FXML
    private Button removeBtn;

    @FXML
    private Button acceptBtn;

    @FXML
    private Button declineBtn;

    @FXML
    private Button sendMessageBtn;

    @FXML
    private TableView<UserDTO> tableView;

    @FXML
    private TableColumn<UserDTO, String> usernameColumn;

    @FXML
    private TableColumn<UserDTO, String> friendsInCommonColumn;

    @FXML
    private TableColumn<UserDTO, String> sinceColumn;

    @FXML
    private TableColumn<UserDTO, String> statusColumn;

    private ObservableList<UserDTO> modelUser = FXCollections.observableArrayList();

    public void setMainWindow(Service service, User loggedUser) {
        this.service = service;
        this.loggedUser = loggedUser;
        this.welcomeText.setText("Hi, " + loggedUser.getUsername() + "!");
    }

    private void disableButtons() {
        addBtn.setDisable(true);
        removeBtn.setDisable(true);
        acceptBtn.setDisable(true);
        declineBtn.setDisable(true);
        sendMessageBtn.setDisable(true);
    }

    @FXML
    private void selectAction() {
        UserDTO userDTO = tableView.getSelectionModel().getSelectedItem();
        disableButtons();
        sendMessageBtn.setDisable(false);

        if (tableLabel.getText().equals("FRIENDS")) {
            removeBtn.setDisable(false);
        }

        if (tableLabel.getText().equals("DISCOVER NEW")) {
            if (userDTO.getStatus().equals("Not friends"))
                addBtn.setDisable(false);
            if (userDTO.getStatus().equals("Pending")) {
                Friendship friendship = service.findFriendshipBy2ids(loggedUser.getId(), userDTO.getId());
                if (friendship.getId1().equals(loggedUser.getId())) {
                    removeBtn.setDisable(false);
                } else if (friendship.getId2().equals(loggedUser.getId())) {
                    acceptBtn.setDisable(false);
                    declineBtn.setDisable(false);
                }
            }
        }

        if (userDTO.getStatus().equals("Pending")) {
            if (tableLabel.getText().equals("SENT REQUESTS")) {
                removeBtn.setDisable(false);
            }

            if (tableLabel.getText().equals("REQUESTS")) {
                acceptBtn.setDisable(false);
                declineBtn.setDisable(false);
            }
        }
    }

    @FXML
    private void initialize() {
        disableButtons();
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        friendsInCommonColumn.setCellValueFactory(new PropertyValueFactory<>("friendsInCommon"));
        sinceColumn.setCellValueFactory(new PropertyValueFactory<>("since"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableView.setItems(modelUser);
    }

    private List<UserDTO> getFriendsDTO() {
        List<Long> friends = service.getAllFriendsForUser(loggedUser.getId());
        return friends.stream().map(x -> service.findUser(x)).toList().stream()
                .map(x -> new UserDTO(x.getId(), x.getUsername(),
                        service.getFriendsInCommon(loggedUser.getId(), x.getId()),
                        service.getFriendsFrom(loggedUser.getId(), x.getId()), "accepted"))
                .collect(Collectors.toList());
    }

    private List<UserDTO> getNotFriendsDTO() {
        List<User> friends = service.getAllFriendsForUser(loggedUser.getId()).stream().map(x -> service.findUser(x)).toList();
        List<User> notFriends = (List<User>) service.findAllUsers();
        notFriends.removeAll(friends);
        notFriends.remove(loggedUser);
        return notFriends.stream()
                .map(x -> new UserDTO(x.getId(), x.getUsername(),
                        service.getFriendsInCommon(loggedUser.getId(), x.getId()),
                        service.getFriendshipStatus(loggedUser.getId(), x.getId())))
                .collect(Collectors.toList());
    }

    private List<UserDTO> getSentRequestsDTO() {
        List<Long> idUsers = service.getSentRequestUsers(loggedUser.getId());

        return idUsers.stream().map(x -> service.findUser(x)).toList().stream()
                .map(x -> new UserDTO(x.getId(), x.getUsername(),
                        service.getFriendsInCommon(loggedUser.getId(), x.getId()),
                        service.getRequestDate(loggedUser.getId(), x.getId()),
                        service.getFriendshipStatus(loggedUser.getId(), x.getId())))
                .collect(Collectors.toList());
    }

    private List<UserDTO> getRequestsDTO() {
        List<Long> idUsers = service.getRequestUsers(loggedUser.getId());

        return idUsers.stream().map(x -> service.findUser(x)).toList().stream()
                .map(x -> new UserDTO(x.getId(), x.getUsername(),
                        service.getFriendsInCommon(loggedUser.getId(), x.getId()),
                        service.getRequestDate(loggedUser.getId(), x.getId()),
                        service.getFriendshipStatus(loggedUser.getId(), x.getId())))
                .collect(Collectors.toList());
    }

    private void setModel(){
        if (tableLabel.getText().equals("FRIENDS")){
            modelUser.setAll(getFriendsDTO());
        }

        if (tableLabel.getText().equals("REQUESTS")){
            modelUser.setAll(getRequestsDTO());
        }

        if (tableLabel.getText().equals("DISCOVER NEW")){
            modelUser.setAll(getNotFriendsDTO());
        }

        if (tableLabel.getText().equals("SENT REQUESTS")){
            modelUser.setAll(getSentRequestsDTO());
        }
    }

    @FXML
    public void friendsBtnClicked() {
        tableLabel.setText("FRIENDS");
        sinceColumn.setText("Friends since");
        statusColumn.setVisible(false);
        sinceColumn.setVisible(true);
        modelUser.setAll(getFriendsDTO());

        if (tableView.getSelectionModel().isEmpty()) {
            disableButtons();
        }
    }

    @FXML
    public void discoverNewBtnClicked() {
        tableLabel.setText("DISCOVER NEW");
        sinceColumn.setVisible(false);
        statusColumn.setVisible(true);
        modelUser.setAll(getNotFriendsDTO());

        if (tableView.getSelectionModel().isEmpty()) {
            disableButtons();
        }
    }

    @FXML
    public void addBtnClicked() {
        UserDTO userDTO = tableView.getSelectionModel().getSelectedItem();
        service.sendRequest(loggedUser.getId(), userDTO.getId());
        tableView.getSelectionModel().clearSelection();
        setModel();
    }

    @FXML
    public void removeBtnClicked() {
        UserDTO userDTO = tableView.getSelectionModel().getSelectedItem();
        Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this friend?", ButtonType.YES, ButtonType.CANCEL);
        Optional<ButtonType> result = removeAlert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.YES){
            service.deleteFriend(loggedUser.getId(), userDTO.getId());
        }
        tableView.getSelectionModel().clearSelection();
        setModel();
    }

    @FXML
    public void acceptBtnClicked(){
        UserDTO userDTO = tableView.getSelectionModel().getSelectedItem();
        Friendship friendship = service.findFriendshipBy2ids(loggedUser.getId(), userDTO.getId());
        service.updateFriendship(friendship.getId(), LocalDate.now(), "Accepted");
        tableView.getSelectionModel().clearSelection();
        setModel();
    }

    @FXML
    public void declineBtnClicked(){
        UserDTO userDTO = tableView.getSelectionModel().getSelectedItem();
        service.deleteFriend(loggedUser.getId(), userDTO.getId());
        tableView.getSelectionModel().clearSelection();
        setModel();
    }

    @FXML
    public void sentRequestsBtnClicked() {
        tableLabel.setText("SENT REQUESTS");
        sinceColumn.setText("Request date");
        sinceColumn.setVisible(true);
        statusColumn.setVisible(true);
        modelUser.setAll(getSentRequestsDTO());

        if (tableView.getSelectionModel().isEmpty()) {
            disableButtons();
        }
    }

    @FXML
    public void requestsBtnClicked() {
        tableLabel.setText("REQUESTS");
        sinceColumn.setText("Request date");
        sinceColumn.setVisible(true);
        statusColumn.setVisible(true);
        modelUser.setAll(getRequestsDTO());

        if (tableView.getSelectionModel().isEmpty()) {
            disableButtons();
        }
    }

    @FXML
    public void sendMessageBtnClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/toysocialnetwork/chat.fxml"));
        AnchorPane root = fxmlLoader.load();
        Chat chat = fxmlLoader.getController();
        UserDTO recieverDTO = tableView.getSelectionModel().getSelectedItem();
        User reciever = service.findUser(recieverDTO.getId());
        chat.setChat(service, loggedUser, reciever);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("TSN");
        stage.show();
    }

    @FXML
    public void logoutAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/toysocialnetwork/login.fxml"));
        VBox root = fxmlLoader.load();
        Login login = fxmlLoader.getController();
        login.setService(service);
        login.setMsgToUser("Successfully logged out.");

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("TSN");
        stage.show();

        closeWindow();
    }

    public void closeWindow() {
        Stage thisStage = (Stage) welcomeText.getScene().getWindow();
        thisStage.close();
    }
}
