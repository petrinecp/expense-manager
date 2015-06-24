package cz.muni.expense.rest.exception.handler;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDto {

    private List<FieldErrorDto> fieldErrors = new ArrayList<>();
 
    public ValidationErrorDto() {
 
    }
 
    public void addFieldError(String path, String message) {
        FieldErrorDto error = new FieldErrorDto(path, message);
        fieldErrors.add(error);
    }

    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorDto> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
 
}
