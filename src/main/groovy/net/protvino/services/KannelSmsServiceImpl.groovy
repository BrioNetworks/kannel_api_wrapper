package net.protvino.services

import net.protvino.entities.Sms
import net.protvino.entities.SmsRequest
import net.protvino.repositories.SmsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder

import java.time.LocalDateTime

@Service
class KannelSmsServiceImpl implements SmsService {

    @Autowired
    SmsRepository smsRepository

    @Value("\${kannel.smsbox.schema}")
    String scheme
    @Value("\${kannel.smsbox.host}")
    String host
    @Value("\${kannel.smsbox.port}")
    String port
    @Value("\${kannel.smsbox.path}")
    String path
    @Value("\${kannel.smsbox.from}")
    String from
    @Value("\${kannel.smsbox.login}")
    String user
    @Value("\${kannel.smsbox.password}")
    String pass
    @Value("\${kannel.dlr.host}")
    String dlrHost
    @Value("\${kannel.dlr.port}")
    String dlrPort
    @Value("\${kannel.dlr.path}")
    String dlrPath

    private static final char[] sevenbitdefault =
            ['@', '£', '$', '¥', 'è', 'é', 'ù', 'ì', 'ò', 'Ç', '\n', 'Ø', 'ø',
             '\r', 'Å', 'å', '\u0394', '_', '\u03a6', '\u0393', '\u039b', '\u03a9', '\u03a0', '\u03a8', '\u03a3',
             '\u0398', '\u039e', '€', 'Æ', 'æ', 'ß', 'É', ' ', '!', '"', '#', '¤', '%', '&', '\'', '(', ')', '*', '+',
             ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '¡',
             'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
             'V', 'W', 'X', 'Y', 'Z', 'Ä', 'Ö', 'Ñ', 'Ü', '§', '¿', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
             'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'ä', 'ö', 'ñ', 'ü', 'à']

    public static final Logger logger = LoggerFactory.getLogger(KannelSmsServiceImpl)

    @Override
    Sms send(SmsRequest smsRequest) {

        Sms sms = new Sms(receiver: smsRequest.receiver, text: smsRequest.text)
        sms.updated = sms.created = LocalDateTime.now()
        sms = save(sms)
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.TEXT_HTML)

        ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory()
        factory.setConnectionRequestTimeout(2 * 1000)
        factory.setConnectTimeout(2 * 1000)
        factory.setReadTimeout(2 * 1000)
        RestTemplate restTemplate = new RestTemplate(factory)

        RequestEntity request = RequestEntity.get(composeUri(sms)).build()
        ResponseEntity<String> response = restTemplate.exchange(request, String)
        if (response.statusCode == HttpStatus.ACCEPTED) {
            sms.status = Sms.Status.ACCEPTED
            save(sms)
        } else {
            sms.status = Sms.Status.ERROR
            logger.warn("Kannel experienced problem sending $sms to url: ${request.url}")
            save(sms)
        }
    }

    @Override
    Sms save(Sms sms) {
        smsRepository.save(sms)
    }

    @Override
    Sms findOne(Long id) {
        smsRepository.findOne(id)
    }

    @Override
    Sms dlr(Long smsid, Integer type) {

        Sms sms = findOne(smsid)
        switch (type) {
            case 1:
                sms.status = Sms.Status.SUCCESS
                break
            case 2:
                sms.status = Sms.Status.FAILURE
                break
            case 4:
                sms.status = Sms.Status.BUFFERED
                break
            case 8:
                sms.status = Sms.Status.SUBMIT
                break
            case 16:
                sms.status = Sms.Status.REJECT
                break
        }
        sms.updated = LocalDateTime.now()
        save(sms)
    }

    private URI composeUri(Sms sms) {

        String dlrUrl = "http://$dlrHost:$dlrPort$dlrPath?smsid=${sms.id}&type=%d"

        String coding = "0"
        if (!is7Bit(sms.text)) {
            coding = "2"
        }
        String charset = "UTF-8"

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .port(port)
                .path(path)
                .queryParam("user", user)
                .queryParam("pass", pass)
                .queryParam("from", "+$from")
                .queryParam("to", "+${sms.receiver}")
                .queryParam("text", sms.text)
                .queryParam("coding", coding)
                .queryParam("charset", charset)
                .queryParam("dlr-mask", "31")
                .queryParam("dlr-url", dlrUrl)
                .build()
        logger.debug("Sms was send to url: {}", uriComponents.encode().toUriString())
        uriComponents.encode().toUri()

    }

    /**
     * checks is message string consists only with 7bit alphabet
     *
     * @param text
     * @return
     */
    private static boolean is7Bit(String text) {
        String array = new String(sevenbitdefault)
        for (char character : text.toCharArray()) {
            if (array.indexOf(Character.getNumericValue(character)) == -1)
                return false
        }
        return true
    }

}
