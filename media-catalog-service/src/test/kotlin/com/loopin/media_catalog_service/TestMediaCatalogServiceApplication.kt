package com.loopin.media_catalog_service

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<MediaCatalogServiceApplication>().with(TestcontainersConfiguration::class).run(*args)
}
