package no.acntech.sandbox.service

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import no.acntech.sandbox.model.AccountDto
import no.acntech.sandbox.model.AccountEntity
import no.acntech.sandbox.repository.AccountRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Validated
@Service
class AccountService(private val conversionService: ConversionService,
                     private val accountRepository: AccountRepository) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun upsert(@Valid @NotNull accountDto: AccountDto) {
        try {
            LOGGER.info("Processing account ${accountDto.id}")
            val optionalAccountEntity = accountRepository.findById(accountDto.id)
            if (optionalAccountEntity.isEmpty) {
                val accountEntity = conversionService.convert(accountDto, AccountEntity::class.java)!!
                accountRepository.save(accountEntity)
            } else {
                LOGGER.info("Account ${accountDto.id} already exists")
            }
        } catch (e: Exception) {
            LOGGER.error("Failed to process account ${accountDto.id}")
            throw e
        }
    }

    private companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AccountService::class.java)
    }
}