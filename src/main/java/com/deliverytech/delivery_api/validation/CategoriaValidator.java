package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class CategoriaValidator implements ConstraintValidator<ValidCategoria, String> {

    private final List<String> categoriasValidas = List.of(
            "LANCHE", "PIZZARIA", "JAPONESA", "BRASILEIRA", "CHINESA", "VEGANA", "ITALIANA"
    );

    @Override
    public boolean isValid(String categoria, ConstraintValidatorContext context) {
        if (categoria == null) return false;
        return categoriasValidas.contains(categoria.toUpperCase());
    }
}
