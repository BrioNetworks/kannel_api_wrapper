package net.protvino.validations

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.*

@Documented
@Constraint(validatedBy = PhoneValidator)
@Target([ElementType.METHOD, ElementType.FIELD])
@Retention(RetentionPolicy.RUNTIME)
@interface Phone {
    String message() default "Invalid format, valid format is ^7\\\\[0-9\\\\]\\\\{10\\\\}\$"

    Class<?>[] groups() default []

    Class<? extends Payload>[] payload() default []
}