package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = FutureAfterDateValidator.class)
public @interface FutureAfterDate {
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    String date();
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
