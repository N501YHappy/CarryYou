plugins{
    java
}
val targetJavaVersion = 17
val source = repositories.RoseWoodDev
val sharedSpigotAPI = "1.21"
subprojects{
    apply(plugin = "java")
    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.enginehub.org/repo/")
        maven(source.repository)
    }
}
val shared = project("shared")
shared.dependencies {
    add("compileOnly", "org.spigotmc:spigot-api:$sharedSpigotAPI-R0.1-SNAPSHOT")
}
subprojects {
    dependencies {
        if (name != "shared") add("compileOnly", shared)
    }
}
object repositories{
    class Sources(
        val repository: String,
        val dependency: (String) -> String
    );
    val RoseWoodDev = Sources(
        repository = "https://repo.rosewooddev.io/repository/public/",
        dependency = { zako -> "org.spigotmc:spigot:$zako" }
    )
}