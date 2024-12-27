package sg.snserver.hex.adapter_outbound.jpa.enums

import sg.snserver.hex.domain.enums.PlatformType

enum class PlatformTypeEntity(
    val value: String,
) {
    YOUTUBE("youtube");

    companion object {
        fun fromValue(value: String): PlatformTypeEntity {
            return entries.find { it.value.equals(value, true) }!!
        }
    }

    fun toDomain(): PlatformType = PlatformType.fromValue(value)

}