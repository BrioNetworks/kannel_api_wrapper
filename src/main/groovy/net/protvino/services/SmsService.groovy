package net.protvino.services

import net.protvino.entities.Sms
import net.protvino.entities.SmsRequest

interface SmsService {

    Sms send(SmsRequest smsRequest)

    Sms save(Sms sms)

    Sms findOne(Long id)

    Sms dlr(Long smsid, Integer type)

}