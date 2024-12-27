package sg.snserver.hex.domain.enums

enum class PlatformType(
    val value: String,
) {
    YOUTUBE("youtube");

    companion object {
        fun fromValue(value: String): PlatformType {
            return entries.find { it.value.equals(value, true) }!!
        }
    }
}