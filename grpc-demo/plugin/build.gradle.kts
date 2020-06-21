import com.google.protobuf.gradle.*
import org.gradle.kotlin.dsl.provider.gradleKotlinDslOf
plugins {
    id("java")
    id("com.google.protobuf") version "0.8.12"
    id("org.sonarqube") version "3.0"
}
repositories {
  mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

configurations {
    implementation {
        resolutionStrategy.failOnVersionConflict()
    }
}

sourceSets{
    main {
      java {
        setSrcDirs(setOf("src/main/java", "src/generated/main/java", "src/generated/main/grpc"))
      }
      proto {
        setSrcDirs(setOf("src/main/proto"))
        setIncludes(setOf("**/*.proto"))
      }
    }
}

protobuf {
  generatedFilesBaseDir="$projectDir/src/generated"
  protoc {
    artifact="com.google.protobuf:protoc:3.0.0"
  }
  plugins {
    id("grpc") {
      artifact="io.grpc:protoc-gen-grpc-java:1.29.0"
    }
  }

  generateProtoTasks {
    ofSourceSet("main").forEach { t ->
      t.plugins {
       id("grpc")
      }
    }
  }
}

sonarqube {
    properties {
        property("sonar.projectName", "microservices-starter-kotlin")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.projectKey", "microservices-starter-kotlin-app")
        property("sonar.projectVersion", "${project.version}")
        property("sonar.junit.reportPaths", "${projectDir}/build/test-results/test")
        property("sonar.coverage.jacoco.xmlReportPaths", "${projectDir}/build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.coverage.exclusions", "**/R.kt")
        property("sonar.language", "kotlin")
    }
}


dependencies{
    implementation("com.google.protobuf:protobuf-java:3.0.0")
    implementation("io.grpc:grpc-protobuf:1.29.0")
    implementation("io.grpc:grpc-stub:1.29.0")
    //runtime
    implementation("io.grpc:grpc-netty-shaded:1.29.0")
    //compile
    implementation("org.apache.tomcat:annotations-api:6.0.53")
}
