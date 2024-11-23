package com.suraj.careercraft.service.impl;

import com.suraj.careercraft.service.BeanValidationService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class BeanValidationServiceImpl implements BeanValidationService {
    @Override
    public String getValidationErrors(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder("Validation errors: ");
        bindingResult.getFieldErrors().forEach(error ->
                errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return errorMessage.toString();
    }
}
