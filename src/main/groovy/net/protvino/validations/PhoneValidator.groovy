package net.protvino.validations

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    void initialize(Phone constraintAnnotation) {

    }

    @Override
    boolean isValid(String receiver, ConstraintValidatorContext context) {
        receiver.matches("^7[0-9]{10}\$")
    }
}
