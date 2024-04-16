import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("maven-publish")
    id("java")
    id("jacoco")
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
}

version = "${project.property("version")}"
group = "${project.property("group")}"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven(url = "https://sibmaks.ru/repository/maven-snapshots/")
    maven(url = "https://sibmaks.ru/repository/maven-releases/")
}

dependencies {
    compileOnly("org.projectlombok", "lombok", "${project.property("lib_lombok_version")}")
    annotationProcessor("org.projectlombok", "lombok", "${project.property("lib_lombok_version")}")

    implementation("org.springframework.boot", "spring-boot-starter-cache")
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
    implementation("org.springframework.boot", "spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot", "spring-boot-starter-web")

    implementation("org.flywaydb", "flyway-core", "${project.property("lib_flyway_core_version")}")

    implementation("org.xerial", "sqlite-jdbc")
    implementation("org.hibernate.orm", "hibernate-community-dialects")

    implementation("com.github.simple-mocks", "api-error", "${project.property("lib_api_error_version")}")
    implementation(
        "com.github.simple-mocks",
        "service-error-local",
        "${project.property("lib_service_error_local_version")}"
    )

    implementation("com.github.simple-mocks", "api-session", "${project.property("lib_api_session_version")}")
    implementation(
        "com.github.simple-mocks",
        "service-session-local",
        "${project.property("lib_service_session_local_version")}"
    )

    implementation("com.github.simple-mocks", "api-storage", "${project.property("lib_api_storage_version")}")
    implementation(
        "com.github.simple-mocks",
        "service-storage-local",
        "${project.property("lib_service_storage_local_version")}"
    )

    implementation(project(path = ":common-api"))


    implementation("org.graalvm.js", "js", "${project.property("lib_graalvm_js")}")
    implementation("org.graalvm.js", "js-scriptengine", "${project.property("lib_graalvm_js")}")

    developmentOnly("org.springframework.boot", "spring-boot-devtools")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "${project.property("lib_junit_version")}")
    testImplementation("org.junit.jupiter", "junit-jupiter-params", "${project.property("lib_junit_version")}")

    testImplementation("org.mockito", "mockito-core", "${project.property("lib_mockito_version")}")
    testImplementation("org.mockito", "mockito-junit-jupiter", "${project.property("lib_mockito_version")}")

    testCompileOnly("org.projectlombok", "lombok", "${project.property("lib_lombok_version")}")
    testAnnotationProcessor("org.projectlombok", "lombok", "${project.property("lib_lombok_version")}")

    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "${project.property("lib_junit_version")}")
}

val targetJavaVersion = "${project.property("jdk_version")}".toInt()
tasks.withType<JavaCompile>().configureEach {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release = targetJavaVersion
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.property("project_name")}" }
    }
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to project.name,
                "Specification-Vendor" to project.property("author"),
                "Specification-Version" to project.version,
                "Specification-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
                "Timestamp" to System.currentTimeMillis(),
                "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})"
            )
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                packaging = "jar"
                url = "https://github.com/simple-mocks/simple-mocks"

                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://www.mit.edu/~amini/LICENSE.md")
                    }
                }

                scm {
                    connection.set("scm:https://github.com/simple-mocks/simple-mocks.git")
                    developerConnection.set("scm:git:ssh://github.com/sib-energy-craft")
                    url.set("https://github.com/simple-mocks/simple-mocks")
                }

                developers {
                    developer {
                        id.set("sibmaks")
                        name.set("Maksim Drobyshev")
                        email.set("sibmaks@vk.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val releasesUrl = uri("https://sibmaks.ru/repository/maven-releases/")
            val snapshotsUrl = uri("https://sibmaks.ru/repository/maven-snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = project.findProperty("nexus_username")?.toString() ?: System.getenv("NEXUS_USERNAME")
                password = project.findProperty("nexus_password")?.toString() ?: System.getenv("NEXUS_PASSWORD")
            }
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/simple-mocks/simple-mocks")
            credentials {
                username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key")?.toString() ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
