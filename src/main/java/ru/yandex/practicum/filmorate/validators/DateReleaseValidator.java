package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateReleaseValidator implements ConstraintValidator<DateReleaseConstraint, LocalDate> {

    @Override
    public void initialize(DateReleaseConstraint localDate) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext cxt) {
        return localDate.isAfter(LocalDate.of(1895, 12, 27));
    }
}