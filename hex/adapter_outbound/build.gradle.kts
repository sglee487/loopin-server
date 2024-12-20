dependencies {
    implementation((project(":domain")))
    implementation((project(":application")))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // database
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.3.0")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // google api (youtube)
    implementation("com.google.api-client:google-api-client:2.6.0")
    implementation("com.google.oauth-client:google-oauth-client:1.36.0")
    implementation("com.google.apis:google-api-services-youtube:v3-rev20240514-2.0.0")
    implementation("com.google.http-client:google-http-client-jackson2:1.44.2")
    implementation("com.google.api.client:google-api-client-extensions:1.4.1-beta")
    implementation("com.google.api-client:google-api-client-java6:2.1.4")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.36.0")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
}