package no.acntech.sandbox.converter

import no.acntech.sandbox.model.StatusDto
import no.acntech.sandbox.model.StatusEntity
import org.springframework.core.convert.converter.Converter
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component

@Component
class StatusDtoToStatusEntityConverter() : Converter<StatusDto, StatusEntity> {

    @NonNull
    override fun convert(@NonNull source: StatusDto): StatusEntity {
        return StatusEntity(
                source.id,
                source.url.toString(),
                source.language,
                source.visibility,
                source.content,
                source.inReplyToStatusId,
                source.inReplyToAccountId,
                source.repliesCount,
                source.reblogsCount,
                source.favouritesCount,
                source.createdAt,
                null
        )
    }
}