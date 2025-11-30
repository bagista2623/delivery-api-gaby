package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoriaValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategoria {
    String message() default "Categoria inválida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
