import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

val coverageThreshold = 0.95
val jvmTargetVersion = JavaVersion.VERSION_1_8.toString()
val publicationName = "maven"
val dokkaDir = "$projectDir/docs/dokka"

plugins {
    jacoco
    `maven-publish`
    Dependencies.plugins.forEach { (n, v) -> id(n) version v }
}

group = "com.github.nwillc"
version = "0.4.6-SNAPSHOT"

logger.lifecycle("${project.group}.${project.name}@${project.version}")

repositories {
    jcenter()
}

dependencies {
    Dependencies.artifacts(
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    ) { implementation(it) }

    Dependencies.artifacts(
        "org.apache.poi:poi",
        "org.apache.poi:poi-ooxml"
    ) { api(it) }

    Dependencies.artifacts(
        "io.mockk:mockk",
        "org.assertj:assertj-core",
        "org.junit.jupiter:junit-jupiter"
    ) { testImplementation(it) }
}

ktlint {
    version.set(ToolVersions.ktlint)
    disabledRules.set(setOf("import-ordering"))
}

detekt {
    input = files("src/main/kotlin")
    reports {
        xml.enabled = true
        html.enabled = true
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.convention("sources")
    from(sourceSets["main"].allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn("dokka")
    archiveClassifier.convention("javadoc")
    from("$buildDir/dokka")
}

jacoco {
    toolVersion = ToolVersions.jacoco
}

publishing {
    publications {
        create<MavenPublication>(publicationName) {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javadocJar.get())
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_API_KEY")
    dryRun = false
    publish = true
    setPublications(publicationName)
    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
        repo = publicationName
        name = project.name
        desc = "Apache POI Koltin DSL"
        websiteUrl = "https://github.com/nwillc/poink"
        issueTrackerUrl = "https://github.com/nwillc/poink/issues"
        vcsUrl = "https://github.com/nwillc/poink.git"
        version.vcsTag = "v${project.version}"
        setLicenses("ISC")
        setLabels("kotlin", "Apache POI", "DSL")
        publicDownloadNumbers = true
    })
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = jvmTargetVersion
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = jvmTargetVersion
    }
    named("check") {
        dependsOn(":jacocoTestCoverageVerification")
    }
    named<Jar>("jar") {
        manifest.attributes["Automatic-Module-Name"] = "${project.group}.${project.name}"
    }
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            events("passed", "failed", "skipped")
        }
    }
    withType<JacocoReport> {
        dependsOn("test")
        reports {
            xml.apply {
                isEnabled = true
            }
            html.apply {
                isEnabled = true
            }
        }
    }
    jacocoTestCoverageVerification {
        dependsOn("jacocoTestReport")
        violationRules {
            rule {
                limit {
                    minimum = coverageThreshold.toBigDecimal()
                }
            }
        }
    }
    withType<GenerateMavenPom> {
        destination = file("$buildDir/libs/${project.name}-${project.version}.pom")
    }
    withType<com.jfrog.bintray.gradle.tasks.BintrayUploadTask> {
        onlyIf {
            if (project.version.toString().contains('-')) {
                logger.lifecycle("Version v${project.version} is not a release version - skipping upload.")
                false
            } else {
                true
            }
        }
    }
    withType<DokkaTask> {
        outputFormat = "html"
        outputDirectory = dokkaDir
        configuration {
            jdkVersion = 8
            externalDocumentationLink {
                url = URL("http://poi.apache.org/apidocs/4.1/")
                packageListUrl = URL("file://${project.rootDir}/docs/poi-package-list")
            }
        }
    }
}
