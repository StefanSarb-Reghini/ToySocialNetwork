package com.example.toysocialnetwork;

import com.example.toysocialnetwork.domain.Friendship;
import com.example.toysocialnetwork.domain.validators.FriendshipValidator;
import com.example.toysocialnetwork.domain.validators.MessageValidator;
import com.example.toysocialnetwork.domain.validators.UserValidator;
import com.example.toysocialnetwork.gui.Login;
import com.example.toysocialnetwork.repository.Repository;
import com.example.toysocialnetwork.repository.db.FriendshipDbRepository;
import com.example.toysocialnetwork.repository.db.MessageDbRepository;
import com.example.toysocialnetwork.repository.db.UserDbRepository;
import com.example.toysocialnetwork.repository.factory.RepositoryFactory;
import com.example.toysocialnetwork.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.toysocialnetwork.repository.factory.RepositoryEntity.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        UserDbRepository userRepository = (UserDbRepository) RepositoryFactory.getInstance().createRepository(USER);
        FriendshipDbRepository friendshipRepository = (FriendshipDbRepository) RepositoryFactory.getInstance().createRepository(FRIENDSHIP);
        MessageDbRepository messageRepository = (MessageDbRepository) RepositoryFactory.getInstance().createRepository(MESSAGE);
        Service service = new Service(userRepository, friendshipRepository, messageRepository);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("TSN");
        Login login = fxmlLoader.getController();
        login.setService(service);
        System.out.println("Service set");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}