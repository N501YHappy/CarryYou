plugins {
    id("java")
}

subprojects {
    apply(plugin = "java")
    if (name != "shared"){
        apply(plugin = "java-library")
        apply(plugin = "io.papermc.paperweight.userdev")
        dependencies {
            compileOnly(project(":nms:shared"))
        }
    }

    repositories {
        mavenCentral()
    }
}