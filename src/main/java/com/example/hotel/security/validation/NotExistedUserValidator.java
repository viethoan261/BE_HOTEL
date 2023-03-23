package com.example.hotel.security.validation;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.hotel.model.UserModel;
import com.example.hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class NotExistedUserValidator implements ConstraintValidator<NotExistedUser, String> {
    @Autowired
    private UserRepository repository;

    private String message;

    @Override
    public void initialize(NotExistedUser notExistedUser) {
        message = notExistedUser.message();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null)
            return false;

        Optional<UserModel> userOpt = repository.findByUserName(username);

        if (userOpt.isEmpty()) {
            return true;
        }

        context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }

}
