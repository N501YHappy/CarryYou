import org.gradle.language.jvm.tasks.ProcessResources
plugins {
    java
    id("com.gradleup.shadow") version "9.3.0"
}

group = "xyz.n501yhappy"
version = "2.4"

repositories {
    maven("https://mirrors.huaweicloud.com/repository/maven")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")

    maven("https://maven.enginehub.org/repo/") //Worldguard
}

configurations.create("shadowLink")

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.0")
    compileOnly(fileTree("libs"))
    compileOnly("cn.lunadeer:DominionAPI:4.7.3")
    implementation(project(":adapts:shared"))
    add("shadowLink", project(":adapts:folia"))
    add("shadowLink", project(":adapts:bukkit"))
}

tasks {
    shadowJar {
        configurations.add(project.configurations.getByName("shadowLink"))
        // Shadow 9.x 需要手动指定 runtimeClasspath
        configurations.add(project.configurations.runtimeClasspath.get())
        // 重命名包避免和别的插件冲突
        relocate("adapts.impl", "xyz.n501yhappy.carryyou.adapts")
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }
}
