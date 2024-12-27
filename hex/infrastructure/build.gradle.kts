dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":adapter_inbound"))
    implementation(project(":adapter_outbound"))

    // database
    implementation("org.postgresql:postgresql:42.7.2")

}
