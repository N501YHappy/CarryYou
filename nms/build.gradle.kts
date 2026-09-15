plugins {
    id("java")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "io.papermc.paperweight.userdev")
    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.rosewooddev.io/repository/public/")
        maven("https://repo.codemc.io/repository/nms/")
    }
}