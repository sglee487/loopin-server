package com.loopin.media_catalog_service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class MediaCatalogServiceApplicationTests {

	@Test
	fun contextLoads() {
	}

}
