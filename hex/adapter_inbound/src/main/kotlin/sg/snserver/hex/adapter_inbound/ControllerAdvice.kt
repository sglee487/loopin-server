package sg.snserver.hex.adapter_inbound

import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import sg.snserver.hex.application.AlreadyExistsException

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(AlreadyExistsException::class)
    fun handleAlreadyExistsException(e: AlreadyExistsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.notFound().build()
    }
}