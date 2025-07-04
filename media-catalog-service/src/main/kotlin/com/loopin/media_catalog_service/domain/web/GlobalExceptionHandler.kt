package com.loopin.media_catalog_service.domain.web

import com.loopin.media_catalog_service.domain.exception.AlreadyExistsException
import com.loopin.media_catalog_service.domain.exception.NotExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyExistsException::class)
    fun handleAlreadyExistsException(ex: AlreadyExistsException): ResponseEntity<Map<String, String>> {
        val body = mapOf("message" to (ex.message ?: "Resource already exists"))
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body)
    }
    @ExceptionHandler(NotExistsException::class)
    fun handleNotExistsException(ex: NotExistsException): ResponseEntity<Map<String, String>> {
        val body = mapOf("message" to (ex.message ?: "Resource not exists"))
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }
}
