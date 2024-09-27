plugins {
    java
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(libs.geyser.core)
    implementation(libs.configurate.hocon)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set(null as String?)

        listOf(
            "org.spongepowered",
        ).forEach {
            relocate(it, "org.stupidcraft.geyserdebuginfo.lib.$it")
        }
    }

    processResources {
        filesMatching(listOf("extension.yml")) {
            expand("version" to project.version)
        }
    }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)
