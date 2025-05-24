package com.esig.taskmanager.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.time.LocalDate;

@FacesValidator("dateNotPastValidator")
public class DateNotPastValidator implements Validator<LocalDate> {
    @Override
    public void validate(FacesContext context, UIComponent component, LocalDate value) {
        if (value != null && value.isBefore(LocalDate.now())) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "A data não pode estar no passado", null));
        }
    }
}

