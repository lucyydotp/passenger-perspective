version = "0.1.4"
val MAIN_MINECRAFT_VERSION = "1.20.1"
val ADDITIONAL_MINECRAFT_VERSIONS = setOf<String>()

plugins {
    id("java")
    id("fabric-loom") version "1.6-SNAPSHOT"
    id("me.modmuss50.mod-publish-plugin") version "0.5.1"
}

group = "me.lucyydotp"

// When running in CI from a tag, set the version number to the tag name
if (System.getenv("CI") != null && System.getenv("GITHUB_REF_TYPE") == "tag") {
    val tag = System.getenv("GITHUB_REF_NAME")
    if (tag.startsWith("v")) {
        version = tag.substring(1)
    }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:$MAIN_MINECRAFT_VERSION")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.92.2+1.20.1")
}


tasks {
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

fun Collection<String>.joinFriendly() = when (size) {
    0 -> ""
    1 -> first()
    2 -> "${first()} and ${last()}"
    else -> take(size - 1).joinToString(", ") + ", and ${last()}"
}

publishMods {
    file.set(tasks.remapJar.flatMap { it.archiveFile })

    displayName = project.version as String
    type = STABLE
    modLoaders.add("fabric")

    changelog = providers.provider {
        listOf(
            project.version,
            "footer"
        ).joinToString("\n") { file("changelogs/$it.md").readText() }
    }

    curseforge {
        projectId = "1034905"
        projectSlug = "passenger-perspective"
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions = ADDITIONAL_MINECRAFT_VERSIONS + MAIN_MINECRAFT_VERSION
        clientRequired = true
        requires("fabric-api")
    }

    modrinth {
        projectId = "TT1ubJJB"
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
        dryRunWebhookUrl = providers.environmentVariable("DISCORD_WEBHOOK")

        username = "Passenger Perspective Updates"

        listOf("modrinth")

        setPlatforms(platforms["modrinth"], platforms["curseforge"])

        content = """
            ### Passenger Perspective v${project.version} for Minecraft ${(listOf(MAIN_MINECRAFT_VERSION) + ADDITIONAL_MINECRAFT_VERSIONS).joinFriendly()} has been released!
            ${file("changelogs/${project.version}.md").readText().trim()}      
        """.trimIndent() + "\n\n<@&1250171712671056002>"
    }
}
