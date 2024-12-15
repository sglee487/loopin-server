dependencies {
    implementation(project(":infrastructure"))
    implementation(project(":adapter_inbound"))
    implementation(project(":adapter_outbound"))
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    testImplementation(kotlin("test"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.3.0")

}

springBoot {
    mainClass.set("sg.snserver.cmd.ApplicationKt")
}