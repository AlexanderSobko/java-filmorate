package ru.yandex.practicum.filmorate.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FutureAfterDateValidator implements ConstraintValidator<FutureAfterDate, LocalDate> {

    LocalDate date;
    @Override
    public boolean isValid(LocalDate validationDate, ConstraintValidatorContext constraintValidatorContext) {
        return validationDate.isAfter(date);
    }

    @Override
    public void initialize(FutureAfterDate constraintAnnotation) {
        this.date = LocalDate.parse(constraintAnnotation.date());
    }
}
