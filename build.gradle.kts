import net.fabricmc.loom.task.RemapJarTask


val MAIN_MINECRAFT_VERSION = "1.19.4"
val ADDITIONAL_MINECRAFT_VERSIONS = setOf<String>()

plugins {
    id("java")
    id("fabric-loom") version "1.6-SNAPSHOT"
    id("me.modmuss50.mod-publish-plugin") version "0.5.1"
}

group = "me.lucyydotp"
version = "0.1.2"

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:$MAIN_MINECRAFT_VERSION")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.87.2+1.19.4")
}


tasks {
    withType<RemapJarTask>().configureEach {
    }

    remapJar {
        archiveClassifier = "mc$MAIN_MINECRAFT_VERSION"
    }

    processResources {
        outputs.upToDateWhen { false }
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

publishMods {
    file.set(tasks.remapJar.get().archiveFile)

    changelog = providers.provider {
        listOf(
            version,
            "footer"
        ).joinToString("\n") { file("$it.md").readText() }

    }

    type = ALPHA // STABLE
    modLoaders.add("fabric")

    curseforge {
        projectId = "1034905"
        projectSlug = "passenger-perspective"
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions = ADDITIONAL_MINECRAFT_VERSIONS + MAIN_MINECRAFT_VERSION
        requires("fabric-api")
    }

    modrinth {
        projectId = "papers"
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions = ADDITIONAL_MINECRAFT_VERSIONS + MAIN_MINECRAFT_VERSION
        requires("fabric-api")
    }
    github {
        repository = "lucyydotp/passenger-perspective"
        accessToken = providers.environmentVariable("GITHUB_TOKEN")
        tagName = "v${project.version}"
        commitish = "main"
    }
    discord {
        webhookUrl = providers.environmentVariable("DISCORD_WEBHOOK")
    }
}
