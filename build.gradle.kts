val coverageThreshold = 0.95
val jvmTargetVersion = JavaVersion.VERSION_1_8.toString()
val publicationName = "maven"

val assertJVersion: String by project
val jacocoToolVersion: String by project
val jupiterVersion: String by project
val ktlintToolVersion: String by project
val mockkVersion: String by project
val poiVersion: String by project

plugins {
    kotlin("jvm") version "1.3.61"
    jacoco
    `maven-publish`
    id("com.github.nwillc.vplugin") version "3.0.1"
    id("org.jetbrains.dokka") version "0.10.0"
    id("io.gitlab.arturbosch.detekt") version "1.3.0"
    id("com.jfrog.bintray") version "1.8.4"
    id("org.jlleitschuh.gradle.ktlint") version "9.1.1"
}

group = "com.github.nwillc"
version = "0.1.1"

logger.lifecycle("${project.group}.${project.name}@${project.version}")

repositories {
    jcenter()
}

dependencies {
    listOf(
        kotlin("stdlib-jdk8")
    )
        .forEach { implementation(it) }

    listOf(
        "org.apache.poi:poi:$poiVersion",
        "org.apache.poi:poi-ooxml:$poiVersion"
    )
        .forEach { api(it) }

    listOf(
        "org.junit.jupiter:junit-jupiter-api:$jupiterVersion",
        "org.assertj:assertj-core:$assertJVersion",
        "io.mockk:mockk:$mockkVersion"
    )
        .forEach { testImplementation(it) }

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}

ktlint {
    version.set(ktlintToolVersion)
}

detekt {
    toolVersion = "1.0.0-RC14"
    input = files("src/main/kotlin")
    reports {
        xml {
            enabled = true
        }
        html {
            enabled = true
            destination = file("$buildDir/reports/detekt/detekt.html")
        }
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn("dokka")
    classifier = "javadoc"
    from("$buildDir/dokka")
}

jacoco {
    toolVersion = jacocoToolVersion
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
    withType<org.jetbrains.dokka.gradle.DokkaTask> {
        outputFormat = "html"
        outputDirectory = "docs/dokkaHtml"
    }
}
