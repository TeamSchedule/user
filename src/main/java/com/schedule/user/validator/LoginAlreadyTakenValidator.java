package com.schedule.user.validator;

import com.schedule.user.model.request.CreateUserRequest;
import com.schedule.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class LoginAlreadyTakenValidator
        implements ConstraintValidator<LoginAlreadyTakenConstraint, CreateUserRequest> {
    private final UserService userService;

    @Override
    public boolean isValid(CreateUserRequest request, ConstraintValidatorContext context) {
        return !userService.existsByLogin(request.getLogin());
    }
}
