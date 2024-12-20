package sg.snserver.hex.adapter_outbound.jpa.entities

import sg.snserver.hex.domain.entities.Localized

data class LocalizedEntity(
    val title: String,
    val description: String,
) {
    fun toDomain(): Localized = Localized(
        title = title,
        description = description,
    )
}