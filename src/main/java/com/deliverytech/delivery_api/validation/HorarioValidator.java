package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HorarioValidator implements ConstraintValidator<ValidHorario, String> {

    @Override
    public boolean isValid(String horario, ConstraintValidatorContext context) {

        if (horario == null || horario.isBlank()) {
            return false;
        }

        return horario.matches("^([0-9]{2}:[0-9]{2}) às ([0-9]{2}:[0-9]{2})$");
    }
}
