plugins {
    java
}

dependencies {
    compileOnly(libs.geyser.api)
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    processResources {
        filesMatching(listOf("extension.yml")) {
            expand("version" to project.version)
        }
    }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)
