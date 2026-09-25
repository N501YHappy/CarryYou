rootProject.name = "CarryYou"
include(":adapts:folia")

include(":adapts:bukkit")
include(":adapts:shared")

include(":plugin-api")
include(":nms")

File("nms").listFiles()?.forEach { file ->
    if (File(file, "build.gradle.kts").exists()) {
        include(":nms:${file.name}")
    }
}