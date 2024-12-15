plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "hex"
include("cmd")
include("domain")
include("application")
include("infrastructure")
include("adapter_inbound")
include("adapter_outbound")
