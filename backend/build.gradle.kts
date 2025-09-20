plugins {
    application
}

group = "com.gympandro"
version = "0.0.1"

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(25)) }
}

repositories { mavenCentral() }

dependencies {
    // DB + JSON
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.google.code.gson:gson:2.11.0")

    // Flyway (core + dialect Postgres)
    implementation("org.flywaydb:flyway-core:10.17.0")
    implementation("org.flywaydb:flyway-database-postgresql:10.17.0")

    // Test
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
}

application {
    mainClass.set("com.gympandro.app.Main")
}

tasks.test { useJUnitPlatform() }