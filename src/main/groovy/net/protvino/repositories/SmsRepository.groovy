package net.protvino.repositories

import net.protvino.entities.Sms
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.format.annotation.DateTimeFormat

import java.time.LocalDateTime

@RepositoryRestResource
interface SmsRepository extends PagingAndSortingRepository<Sms, Long> {


    @Query("SELECT s FROM Sms s WHERE (created BETWEEN :start AND :end) AND s.status=:status ")
    Page<Sms> findByCreatedAndStatus(
            @Param("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime start,
            @Param("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime end,
            @Param("status")
                    Sms.Status status,
            Pageable pageable)
}
