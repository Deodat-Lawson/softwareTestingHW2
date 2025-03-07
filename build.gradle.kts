plugins {
    id("java")
    jacoco
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitJupiterVersion = "5.11.3"
val jqwikVersion = "1.9.2"

tasks.named<JavaCompile>("compileTestJava") {
    options.compilerArgs.add("-parameters")
}

tasks.test {
    useJUnitPlatform {
        includeEngines("jqwik", "junit-jupiter")
    }

    // Update your include patterns to match your actual test class names
    include("**/*Properties.class")
    include("**/*Test.class")
    include("**/*Tests.class")
    include("**/*PBT.class")
    include("**/*Fixed.class")
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = false
        csv.required = false
        // Set output location to task4/jacocoreport relative to the project root
        html.outputLocation = file("$rootDir/task4/jacocoreport")
    }
}
dependencies {
    testImplementation(platform("org.junit:junit-bom:$junitJupiterVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("net.jqwik:jqwik:$jqwikVersion")
}


jacoco {
    toolVersion = "0.8.12"
    reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}



