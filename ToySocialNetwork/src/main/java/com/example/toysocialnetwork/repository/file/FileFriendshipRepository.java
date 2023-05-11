package com.example.toysocialnetwork.repository.file;

import com.example.toysocialnetwork.domain.Friendship;
import com.example.toysocialnetwork.domain.validators.Validator;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class FileFriendshipRepository extends FileRepository<Long, Friendship> {
    public FileFriendshipRepository(Validator<Friendship> validator, String filename) {
        super(validator, filename);
    }

    @Override
    protected void loadFromFile() {
        File file = new File(this.filename);
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String entityStr = reader.nextLine();
                List<String> entityAsList = List.of(entityStr.split(","));
                Long id = Long.valueOf(entityAsList.get(0));
                Long id1 = Long.valueOf(entityAsList.get(1));
                Long id2 = Long.valueOf(entityAsList.get(2));
                Friendship friendship = new Friendship(id1, id2);
                friendship.setId(id);
                entities.put(id, friendship);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void saveToFile() {
        try {
            FileWriter fileWriter = new FileWriter(this.filename);
            for (Friendship friendship : entities.values()) {
                fileWriter.write(friendship.toFileFormat() + '\n');
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}