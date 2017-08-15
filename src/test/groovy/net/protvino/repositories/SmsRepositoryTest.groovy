package net.protvino.repositories

import net.protvino.BaseTest
import net.protvino.entities.Sms
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.PagedResources
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDate
import java.time.LocalDateTime

@Stepwise
class SmsRepositoryTest extends BaseTest {

    String getBasePath() {
        "api/smses"
    }

    @Autowired
    SmsRepository smsRepository

    @Shared
    Sms sms1 = new Sms(
            id: 1,
            created: LocalDateTime.now(),
            updated: LocalDateTime.now(),
            receiver: "+1234567890",
            text: "some text",
            status: Sms.Status.ACCEPTED
    )

    def "remove all data from table"() {
        given:
        smsRepository.deleteAll()
        expect:
        smsRepository.count() == 0
    }


    def "create one entry in sms table"() {
        given:
        smsRepository.save(sms1)
        expect:
        smsRepository.count() == 1
    }


    def "FindByCreatedAndStatus sms1"() {
        given:
        RequestEntity request = RequestEntity.get(getServerPath("/search/findByCreatedAndStatus" +
                "?start=${LocalDate.now().atTime(0, 0, 0)}&end=${LocalDate.now().atTime(23, 59, 59)}&status=${sms1.status}"
        )).build()
        when:
        ResponseEntity<PagedResources<Sms>> response = restTemplate()
                .exchange(request, new ParameterizedTypeReference<PagedResources<Sms>>() {})
        List<Sms> result = new ArrayList<Sms>(response.getBody().getContent())
        then:
//        request.getUrl().getPath() == "/smses/search/findByCreatedAndStatus"
        response.statusCode == HttpStatus.OK
        result.size() == 1

    }
}
