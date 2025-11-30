package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CepValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCep {
    String message() default "CEP inválido. Use o formato 00000-000 ou 00000000.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
