package com.example.toysocialnetwork.repository.factory;

import com.example.toysocialnetwork.domain.Friendship;
import com.example.toysocialnetwork.domain.Message;
import com.example.toysocialnetwork.domain.User;
import com.example.toysocialnetwork.domain.validators.FriendshipValidator;
import com.example.toysocialnetwork.domain.validators.MessageValidator;
import com.example.toysocialnetwork.domain.validators.UserValidator;
import com.example.toysocialnetwork.repository.Repository;
import com.example.toysocialnetwork.repository.db.FriendshipDbRepository;
import com.example.toysocialnetwork.repository.db.MessageDbRepository;
import com.example.toysocialnetwork.repository.db.UserDbRepository;

public class RepositoryFactory {
    private static RepositoryFactory instance = new RepositoryFactory();

    private static Repository<Long, User> createUserRepository() {
        return new UserDbRepository("jdbc:postgresql://localhost:5432/social", "postgres", "ubb", new UserValidator());
    }

    private static Repository<Long, Friendship> createFriendshipRepository() {
        return new FriendshipDbRepository("jdbc:postgresql://localhost:5432/social", "postgres", "ubb", new FriendshipValidator());
    }

    private static Repository<Long, Message> createMessageRepository() {
        return new MessageDbRepository("jdbc:postgresql://localhost:5432/social", "postgres", "ubb", new MessageValidator());
    }

    public Repository createRepository(RepositoryEntity repositoryEntity) {
        switch (repositoryEntity) {
            case USER -> {
                return createUserRepository();
            }
            case FRIENDSHIP -> {
                return createFriendshipRepository();
            }
            case MESSAGE -> {
                return createMessageRepository();
            }
            default -> {
                return null;
            }
        }
    }

    public static RepositoryFactory getInstance() {
        return instance;
    }
}
