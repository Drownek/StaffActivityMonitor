plugins {
    id("java")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    group = "me.drownek"
    version = "1.2.6"

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
        options.encoding = "UTF-8"
    }
}
