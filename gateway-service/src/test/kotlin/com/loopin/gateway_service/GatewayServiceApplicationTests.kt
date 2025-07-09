package com.loopin.gateway_service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class GatewayServiceApplicationTests {

	@MockitoBean
	lateinit var clientRegistrationRepository: ReactiveClientRegistrationRepository

	companion object {
		private const val REDIS_PORT = 6379

		@Container
		@JvmStatic
		val redis: GenericContainer<*> = GenericContainer(
			DockerImageName.parse("redis:8.0.2")
		).withExposedPorts(REDIS_PORT)

		@DynamicPropertySource
		@JvmStatic
		fun redisProps(reg: DynamicPropertyRegistry) {
			reg.add("spring.redis.host") { redis.host }
			reg.add("spring.redis.port") { redis.getMappedPort(REDIS_PORT) }
		}
	}

	/* ──────────── 3) 컨텍스트 로드 확인 ──────────── */
	@Test
	fun contextLoads() {
		/* 실패 시 SpringContext 로딩 예외 발생 */
	}
}