package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TelefoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTelefone {
    String message() default "Telefone inválido. Use 10 ou 11 dígitos.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
