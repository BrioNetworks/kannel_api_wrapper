package net.protvino

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters

@SpringBootApplication
@EntityScan(basePackageClasses = [KennelApiApplication, Jsr310JpaConverters])
class KennelApiApplication {

    static void main(String[] args) {
        SpringApplication.run KennelApiApplication, args
    }
}
