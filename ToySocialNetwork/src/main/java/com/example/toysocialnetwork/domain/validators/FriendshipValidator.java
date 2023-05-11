package com.example.toysocialnetwork.domain.validators;

import com.example.toysocialnetwork.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship>{
    @Override
    public void validate(Friendship friendship){
        if (friendship.getId1().equals(friendship.getId2())){
            throw new ValidationException("User cannot add itself as a friend!");
        }
    }
}