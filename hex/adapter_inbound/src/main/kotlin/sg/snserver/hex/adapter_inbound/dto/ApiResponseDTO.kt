package sg.snserver.hex.adapter_inbound.dto

sealed class ApiResponseDTO<T>{
    data class Success<T>(
        val message: String,
        val data: T? = null,
    ) : ApiResponseDTO<T>()

    data class Error<T>(
        val message: String,
        val data: T? = null,
    ) : ApiResponseDTO<T>()

    companion object {
        fun emptySuccess(message: String): Success<Unit> {
            return Success(message = message, data = Unit)
        }

        fun emptyError(message: String): Error<Unit> {
            return Error(message = message, data = Unit)
        }
    }
}