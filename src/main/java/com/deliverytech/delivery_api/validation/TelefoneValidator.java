package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

public class TelefoneValidator implements ConstraintValidator<ValidTelefone, String> {

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null) return false;
        return telefone.matches("\\d{10,11}");
    }

    @Documented
    @Constraint(validatedBy = CepValidator.class)
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ValidCEP {
        String message() default "CEP inválido. Use o formato 00000-000 ou 00000000.";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
}
