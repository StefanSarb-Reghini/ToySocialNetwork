package com.example.toysocialnetwork.gui;

import com.example.toysocialnetwork.domain.User;
import com.example.toysocialnetwork.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    private Service service;

    public void setService(Service service) {
        this.service = service;
    }

    public void setMsgToUser(String msgToUser) {
        addedText.setText(msgToUser);
    }

    @FXML
    private TextField emailText;

    @FXML
    private TextField passwordText;

    @FXML
    private Label addedText;

    @FXML
    public void onLoginButtonClick() throws IOException {
        String email = emailText.getText();
        String password = passwordText.getText();
        if (email.isEmpty()) {
            addedText.setText("Please enter your email address.");
        } else if (password.isEmpty()) {
            addedText.setText("Please enter your password.");
        } else {
            User user = service.findUserByEmailPassword(email, password);
            if (user == null) {
                addedText.setText("Invalid credentials! Please try again!");
                passwordText.setText("");
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/toysocialnetwork/main-window.fxml"));
                AnchorPane root = fxmlLoader.load();
                MainWindow mainWindow = fxmlLoader.getController();
                mainWindow.setMainWindow(service, user);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("TSN");
                stage.show();

                //closeWindow();
            }
        }
    }

    @FXML
    public void registerAction() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/toysocialnetwork/register-window.fxml"));
        AnchorPane root = fxmlLoader.load();
        RegisterWindow registerWindow = fxmlLoader.getController();
        registerWindow.setService(service);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("TSN");
        stage.show();

        closeWindow();
    }

    private void closeWindow(){
        Stage thisStage = (Stage) emailText.getScene().getWindow();
        thisStage.close();
    }
}
