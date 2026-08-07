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
    api("com.github.simplified-dev:collections") { version { strictly("652c22d") } }
    api("com.github.simplified-dev:utils") { version { strictly("7c2feb7") } }
    api("com.github.simplified-dev:reflection") { version { strictly("7a28c3a") } }

    // JetBrains Annotations
    api(libs.annotations)

    // Logging
    api(libs.log4j2.api)

    // YAML
    implementation(libs.snakeyaml)

    // Lombok Annotations
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
