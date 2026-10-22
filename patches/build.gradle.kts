group = "app.template"

patches {
    // TODO: Update this section with your project details.
    about {
        name = "WARP Patches"
        description = "Patches for Cloudflare 1.1.1.1 + WARP."
        source = "https://github.com/xx0loxi/patch-vpn"
        author = "xx0loxi"
        contact = "https://github.com/xx0loxi/patch-vpn/issues"
        website = "https://github.com/xx0loxi/patch-vpn"
        license = "GPLv3"
    }
}

// Morphe patch API relies on Kotlin context parameters.
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions.freeCompilerArgs.add("-Xcontext-parameters")
}

// Separate configuration so gson is available at runtime for the
// generatePatchesList task but never bundled into the APK.
val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}
