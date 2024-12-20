dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":adapter_inbound"))
    implementation(project(":adapter_outbound"))

    // database
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.3.0")

}
