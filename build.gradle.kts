plugins {
    id("java")
    id("fabric-loom") version "1.6-SNAPSHOT"
}

group = "me.lucyydotp"
version = "1.0-SNAPSHOT"

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:1.19.4")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.87.2+1.19.4")
}
