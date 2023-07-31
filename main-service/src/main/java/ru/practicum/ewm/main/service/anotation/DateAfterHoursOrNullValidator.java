package ru.practicum.ewm.main.service.anotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateAfterHoursOrNullValidator implements
        ConstraintValidator<DateAfterHoursOrNull, LocalDateTime> {

    LocalDateTime dateTime;

    @Override
    public void initialize(DateAfterHoursOrNull dateAfterHoursOrNull) {
        dateTime = LocalDateTime.now().plusHours(dateAfterHoursOrNull.hours()).truncatedTo(ChronoUnit.SECONDS);
    }

    @Override
    public boolean isValid(LocalDateTime field,
                           ConstraintValidatorContext cxt) {
        return field == null || field.isAfter(dateTime);
    }
}