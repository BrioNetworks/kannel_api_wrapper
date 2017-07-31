package net.protvino.entities

import net.protvino.validations.Phone

import javax.validation.constraints.NotNull

class SmsRequest {

    @NotNull
    @Phone
    String receiver
    @NotNull
    String text
}
