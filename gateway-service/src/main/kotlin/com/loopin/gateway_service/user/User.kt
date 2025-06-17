package com.loopin.gateway_service.user

data class User(
    val username: String,
    val firstName: String,
    val lastName: String,
    val roles: List<String>
)