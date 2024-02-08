package no.acntech.sandbox.repository

import no.acntech.sandbox.model.StatusEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StatusRepository : JpaRepository<StatusEntity, String> {
}