import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
}

group = "site.sg"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    // video streaming
    // https://velog.io/@haerong22/%EC%98%81%EC%83%81-%EC%8A%A4%ED%8A%B8%EB%A6%AC%EB%B0%8D-7.-%EC%8A%A4%ED%8A%B8%EB%A6%AC%EB%B0%8D-HLS-protocol
    // https://dev.to/giboow/how-to-build-a-video-transcoder-with-springboot-and-ffmpeg-n7p
    implementation("com.github.kokorin.jaffree:jaffree:2023.09.10")
    implementation("com.google.code.gson:gson:2.11.0")

    // database
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.3.0")

    // google api (youtube)
    implementation("com.google.api-client:google-api-client:2.6.0")
    implementation("com.google.oauth-client:google-oauth-client:1.36.0")
    implementation("com.google.apis:google-api-services-youtube:v3-rev20240514-2.0.0")
    implementation("com.google.http-client:google-http-client-jackson2:1.44.2")
    implementation("com.google.api.client:google-api-client-extensions:1.4.1-beta")
    implementation("com.google.api-client:google-api-client-java6:2.1.4")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.36.0")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")


    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
