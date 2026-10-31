plugins {
    id("java-library")
}

group = "dev.simplified"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    // Simplified Libraries
    api("com.github.simplified-dev:collections") { version { strictly("6842129") } }
    api("com.github.simplified-dev:utils") { version { strictly("7cc206f") } }
    api("com.github.simplified-dev:reflection") { version { strictly("cc90b40") } }

    // JetBrains Annotations
    api(libs.annotations)

    // Logging
    api(libs.log4j2.api)

    // YAML
    implementation(libs.snakeyaml)

    // Simplified Annotations
    compileOnly(libs.simplified.annotations)
    annotationProcessor(libs.simplified.annotations)
}
