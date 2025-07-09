package com.loopin.gateway_service.user

import com.loopin.gateway_service.config.SecurityConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.core.oidc.StandardClaimNames
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(CurrentUserController::class)
@Import(SecurityConfig::class)
class CurrentUserControllerTests {

    @Autowired
    lateinit var webClient: WebTestClient

    // Keycloak 네트워크 호출을 막기 위해 레지스트리 빈을 목으로 대체
    @MockitoBean
    lateinit var clientRegistrationRepository: ReactiveClientRegistrationRepository

    @Test
    fun whenNotAuthenticatedThen401() {
        webClient.get()
            .uri("/api/v1/user")
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun whenAuthenticatedThenReturnUser() {
        val expectedUser = User(
            "jon.snow",
            "Jon",
            "Snow",
            listOf("employee", "customer")
        )

        webClient
            .mutateWith(configureMockOidcLogin(expectedUser))
            .get()
            .uri("/api/v1/user")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(User::class.java)
            .value { user -> assertThat(user).isEqualTo(expectedUser) }
    }

    private fun configureMockOidcLogin(
        expectedUser: User
    ): SecurityMockServerConfigurers.OidcLoginMutator =
        SecurityMockServerConfigurers.mockOidcLogin().idToken { builder ->
            builder.claim(
                StandardClaimNames.PREFERRED_USERNAME,
                expectedUser.username
            )
            builder.claim(StandardClaimNames.GIVEN_NAME, expectedUser.firstName)
            builder.claim(StandardClaimNames.FAMILY_NAME, expectedUser.lastName)
            builder.claim("roles", expectedUser.roles)
        }
}