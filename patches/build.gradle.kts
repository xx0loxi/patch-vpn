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

        // The generator loads patches from the packaged bundle, so it needs
        // the jar first. buildAndroid runs afterwards and dexes that bundle.
        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    // Morphe Manager reads the patch catalog from inside the bundle, and the
    // Android patcher needs classes.dex. Publish the dexed bundle with the
    // metadata embedded, mirroring the upstream release layout.
    register<Zip>("publishBundle") {
        description = "Publishes the dexed bundle with embedded metadata into patches/."
        from(zipTree(layout.buildDirectory.file("libs/patches-${project.version}.mpp"))) {
            exclude("patches-bundle.json", "patches-list.json")
        }
        from(rootProject.file("patches-bundle.json"))
        from(rootProject.file("patches-list.json"))
        archiveFileName.set("patches-${project.version}.mpp")
        destinationDirectory.set(layout.projectDirectory)
    }

    // Keep the metadata copies next to the published bundle, as in the
    // upstream/GBoard layout.
    register<Copy>("syncPublishedMetadata") {
        description = "Copies patches-bundle.json and patches-list.json into patches/."
        from(rootProject.file("patches-bundle.json"))
        from(rootProject.file("patches-list.json"))
        into(layout.projectDirectory)
        outputs.upToDateWhen { false }
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }

    named("buildAndroid") {
        dependsOn("generatePatchesList")
        finalizedBy("publishBundle", "syncPublishedMetadata")
    }
}
