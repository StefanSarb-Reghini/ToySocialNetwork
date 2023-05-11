package com.example.toysocialnetwork.repository.file;

import com.example.toysocialnetwork.domain.User;
import com.example.toysocialnetwork.domain.validators.Validator;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class FileUserRepository extends FileRepository<Long, User> {

    public FileUserRepository(Validator<User> validator, String filename) {
        super(validator, filename);
    }

    protected void loadFromFile() {
        File file = new File(this.filename);
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String entityStr = reader.nextLine();
                List<String> entityAsList = List.of(entityStr.split(","));
                Long id = Long.valueOf(entityAsList.get(0));
                String username = entityAsList.get(1);
                String password = entityAsList.get(2);
                String email = entityAsList.get(3);
                User user = new User(username, password, email);
                user.setId(id);
                entities.put(id, user);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected void saveToFile() {
        try {
            FileWriter fileWriter = new FileWriter(this.filename);
            for (User user : entities.values()) {
                fileWriter.write(user.toFileFormat() + '\n');
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
