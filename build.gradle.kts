apply(plugin = "inject-java-compiler-classes")

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    `maven-publish`
}

val javaVersion: String = project.findProperty("javaCompilerVersion") as? String
    ?: throw IllegalStateException("javaCompilerVersion not set in gradle.properties")

val jPatchTask = tasks.named("patchJavaCompilerJar")
jPatchTask.configure {
    outputs.upToDateWhen { false }
}

configurations.create("patchedJavac")

artifacts {
    add("patchedJavac", tasks.named("patchJavaCompilerJar"))
}

publishing {
    publications {
        create<MavenPublication>("patchedJavaCompiler") {
            groupId = "dev.pranav.java"
            artifactId = "javac-android"
            version = javaVersion

            val patchedJar = layout.buildDirectory.file("patchedJavaCompilerJar/nb-javac-${javaVersion}-patched.jar")
            artifact(patchedJar) { builtBy(jPatchTask) }

            pom.withXml {
                val root = asNode()
                val deps = root.appendNode("dependencies")

                fun addDep(group: String, artifact: String, ver: String) {
                    val d = deps.appendNode("dependency")
                    d.appendNode("groupId", group)
                    d.appendNode("artifactId", artifact)
                    d.appendNode("version", ver)
                }

                addDep("com.github.Cosmic-IDE.kotlinc-android", "jaxp", "fce2462f00")
            }
        }
    }

    repositories {
        mavenLocal()
    }
}
