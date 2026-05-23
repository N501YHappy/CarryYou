plugins {
    java
}
val shared = project("shared")
subprojects {
    apply(plugin="java")

    dependencies {
        if (name != "shared") add("compileOnly", shared)
    }
}