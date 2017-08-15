package net.protvino

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.hateoas.hal.Jackson2HalModule
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BaseTest extends Specification {
    @LocalServerPort
    int port

    @Value("\${server.context-path}")
    String path

    TestRestTemplate template = new TestRestTemplate()

    String getBasePath() {
        ""
    }

    URI getServerPath(String endpoint = "") {
        new URI(new StringBuilder("http://localhost:$port$path$basePath").append(endpoint).toString())
    }

    TestRestTemplate restTemplate() {
        ObjectMapper mapper = new ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.registerModule(new Jackson2HalModule())
        mapper.registerModule(new JavaTimeModule())

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter()
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"))
        converter.setObjectMapper(mapper)
        new TestRestTemplate(new RestTemplate(Arrays.asList(converter)))
    }
}
