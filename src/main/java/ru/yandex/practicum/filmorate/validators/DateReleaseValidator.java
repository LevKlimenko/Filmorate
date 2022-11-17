package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateReleaseValidator implements ConstraintValidator<DateReleaseConstraint, LocalDate> {
    private static final LocalDate DAY_WHEN_FILM_ERA_START = LocalDate.of(1895, 12, 27);

    @Override
    public void initialize(DateReleaseConstraint localDate) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext cxt) {
        return localDate.isAfter(DAY_WHEN_FILM_ERA_START);
    }
}