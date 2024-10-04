plugins {
    java
    alias(libs.plugins.shadow)
}

repositories {
    mavenCentral()
    maven("https://repo.opencollab.dev/main/")
}

dependencies {
    compileOnly(libs.geyser.core)
    implementation(libs.configurate.yaml)
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
            relocate(it, "com.triassic.geyserdebuginfo.lib.$it")
        }
    }

    processResources {
        filesMatching("extension.yml") {
            expand("version" to project.version)
        }
    }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)
