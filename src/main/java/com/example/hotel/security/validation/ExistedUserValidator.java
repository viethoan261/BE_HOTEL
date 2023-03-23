package com.example.hotel.security.validation;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.hotel.model.UserModel;
import com.example.hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistedUserValidator implements ConstraintValidator<ExistedUser, String> {
    @Autowired
    private UserRepository repository;

    private String message;

    @Override
    public void initialize(ExistedUser existedUser) {
        message = existedUser.message();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null)
            return false;

        Optional<UserModel> userOpt = repository.findByUserName(username);

        if (userOpt.isPresent()) {
            return true;
        }

        context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }

}
