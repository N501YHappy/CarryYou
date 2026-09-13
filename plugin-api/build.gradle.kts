plugins {
    id("java")
}

group = "xyz.n501yhappy.carryyou"
version = rootProject.version;

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    mavenCentral()
}
dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
}
tasks.withType<JavaCompile>().configureEach {
    options.release = 17
}