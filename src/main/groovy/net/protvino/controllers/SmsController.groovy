package net.protvino.controllers

import net.protvino.entities.Sms
import net.protvino.entities.SmsRequest
import net.protvino.services.SmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

import javax.validation.Valid

@Controller
@RequestMapping(value = "/sms")
class SmsController {

    @Autowired
    SmsService smsService

    @RequestMapping(value = "/dlr")
    ResponseEntity<String> getKannelDlrAction(@RequestParam("smsid") Long smsid, @RequestParam("type") Integer type) {
        smsService.dlr(smsid, type)
        new ResponseEntity<String>(HttpStatus.ACCEPTED)
    }

    @RequestMapping(value = "/send")
    ResponseEntity<Sms> sendSmsAction(@Valid SmsRequest smsRequest) {
        new ResponseEntity<Sms>(smsService.send(smsRequest), HttpStatus.ACCEPTED)
    }

}
