package com.loopin.media_catalog_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class MediaCatalogServiceApplication

fun main(args: Array<String>) {
	runApplication<MediaCatalogServiceApplication>(*args)
}
