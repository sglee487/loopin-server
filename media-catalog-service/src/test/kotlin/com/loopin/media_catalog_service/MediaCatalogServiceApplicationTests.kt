package com.loopin.media_catalog_service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@Import(TestcontainersConfiguration::class)
@ActiveProfiles("test")
@SpringBootTest
class MediaCatalogServiceApplicationTests {

	@Test
	fun contextLoads() {
	}

}
