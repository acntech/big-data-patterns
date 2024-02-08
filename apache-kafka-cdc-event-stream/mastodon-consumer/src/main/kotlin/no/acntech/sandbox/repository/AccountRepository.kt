package no.acntech.sandbox.repository

import no.acntech.sandbox.model.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<AccountEntity, String> {
}