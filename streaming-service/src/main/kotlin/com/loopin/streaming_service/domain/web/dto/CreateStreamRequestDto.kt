package com.loopin.streaming_service.domain.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateStreamRequestDto(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 100, message = "Title must be less than 100 characters")
    val title: String,
    
    @field:Size(max = 500, message = "Description must be less than 500 characters")
    val description: String? = null,
    
    val resolution: String = "720p",
    val bitrate: Int = 2500
)