package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HorarioValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHorario {

    String message() default "Horário inválido. Formato: HH:mm às HH:mm";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
