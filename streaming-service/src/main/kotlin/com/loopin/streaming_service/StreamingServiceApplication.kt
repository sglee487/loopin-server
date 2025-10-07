package com.loopin.streaming_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class])
class StreamingServiceApplication

fun main(args: Array<String>) {
	runApplication<StreamingServiceApplication>(*args)
}

// temp for ci test