package com.example.toysocialnetwork.gui;

import com.example.toysocialnetwork.domain.validators.ValidationException;
import com.example.toysocialnetwork.exceptions.RepositoryException;
import com.example.toysocialnetwork.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterWindow {
    private Service service;

    public void setService(Service service) {
        this.service = service;
    }

    public void setMsgToUser(String msgToUser) {
        messageToUser.setText(msgToUser);
    }

    @FXML
    private TextField usernameText;

    @FXML
    private TextField emailText;

    @FXML
    private TextField passwordText;

    @FXML
    private TextField confpassText;

    @FXML
    private Label messageToUser;

    @FXML
    public void registerClicked() throws IOException {
        if (!passwordText.getText().equals(confpassText.getText())) {
            messageToUser.setText("Passwords do not match!");
            confpassText.clear();
            return;
        }

        try {
            service.addUser(usernameText.getText(), passwordText.getText(), emailText.getText());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/toysocialnetwork/login.fxml"));
            VBox root = fxmlLoader.load();
            Login login = fxmlLoader.getController();
            login.setService(service);
            login.setMsgToUser("User created. Now login");

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("TSN");
            stage.show();

            closeWindow();

        } catch (ValidationException | RepositoryException validationException) {
            messageToUser.setText(validationException.getMessage());
        }
    }

    private void closeWindow() {
        Stage thisStage = (Stage) emailText.getScene().getWindow();
        thisStage.close();
    }
}
