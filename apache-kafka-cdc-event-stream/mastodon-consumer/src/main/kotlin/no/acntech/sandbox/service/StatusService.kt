package no.acntech.sandbox.service

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import no.acntech.sandbox.model.StatusDto
import no.acntech.sandbox.model.StatusEntity
import no.acntech.sandbox.repository.AccountRepository
import no.acntech.sandbox.repository.StatusRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Validated
@Service
class StatusService(private val conversionService: ConversionService,
                    private val accountRepository: AccountRepository,
                    private val statusRepository: StatusRepository) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun upsert(@Valid @NotNull statusDto: StatusDto) {
        try {
            LOGGER.info("Processing status ${statusDto.id} from account ${statusDto.account.id}")
            val optionalStatusDto = statusRepository.findById(statusDto.id)
            if (optionalStatusDto.isEmpty) {
                val statusEntity = conversionService.convert(statusDto, StatusEntity::class.java)!!
                val optionalAccountEntity = accountRepository.findById(statusDto.account.id)
                if (optionalAccountEntity.isPresent) {
                    statusEntity.account = optionalAccountEntity.get()
                    statusRepository.save(statusEntity)
                } else {
                    LOGGER.error("Account ${statusDto.account.id} does not exist")
                }
            } else {
                LOGGER.warn("Status ${statusDto.id} already exists")
            }
        } catch (e: Exception) {
            LOGGER.error("Failed to process status ${statusDto.id} from account ${statusDto.account.id}")
            throw e
        }
    }

    private companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(StatusService::class.java)
    }
}