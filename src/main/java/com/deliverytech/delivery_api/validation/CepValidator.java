package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CepValidator implements ConstraintValidator<ValidCep, String> {

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null) return false;
        return cep.matches("\\d{5}-\\d{3}") || cep.matches("\\d{8}");
    }
}
