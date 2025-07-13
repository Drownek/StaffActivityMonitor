dependencies {
    implementation("eu.okaeri:okaeri-configs-json-simple:5.0.9")
    implementation("eu.okaeri:okaeri-persistence-jdbc:3.0.1-beta.3")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.7.3")

    /* lombok */
    val lombok = "1.18.32"
    compileOnly("org.projectlombok:lombok:$lombok")
    annotationProcessor("org.projectlombok:lombok:$lombok")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}