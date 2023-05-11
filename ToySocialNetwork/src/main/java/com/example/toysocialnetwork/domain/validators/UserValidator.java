package com.example.toysocialnetwork.domain.validators;

import com.example.toysocialnetwork.domain.User;

public class UserValidator implements Validator<User>{

    @Override
    public void validate(User entity) throws ValidationException{
        String msg = "";
        if(entity.getUsername().isEmpty()){
            msg += "Username field can't be empty!\n";
        }

        if(!entity.getEmail().contains("@")){
            msg += "Incorrect format for email adress! Must conatin '@'.\n";
        }

        if(entity.getPassword().length() < 4 || entity.getPassword().length() > 32){
            msg += "Password must have between 4 and 32 characters!\n";
        }

        if(!msg.isEmpty()) throw new ValidationException(msg);
    }
}
