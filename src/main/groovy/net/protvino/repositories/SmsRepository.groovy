package net.protvino.repositories

import net.protvino.entities.Sms
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface SmsRepository extends PagingAndSortingRepository<Sms, Long> {
}
