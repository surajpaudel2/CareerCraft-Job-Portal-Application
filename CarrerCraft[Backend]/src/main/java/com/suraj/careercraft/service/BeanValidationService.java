package com.suraj.careercraft.service;

import org.springframework.validation.BindingResult;

public interface BeanValidationService {
    String getValidationErrors(BindingResult bindingResult);
}
