plugins {
    java
}
val shared = project("shared")
subprojects {
    apply(plugin="java")
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }
    dependencies {
        if (name != "shared") add("compileOnly", shared)
    }
}