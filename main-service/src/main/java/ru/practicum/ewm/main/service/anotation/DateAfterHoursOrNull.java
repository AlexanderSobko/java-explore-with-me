package ru.practicum.ewm.main.service.anotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateAfterHoursOrNullValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateAfterHoursOrNull {

    String message();

    int hours();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
