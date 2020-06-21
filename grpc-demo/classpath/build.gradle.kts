import com.google.protobuf.gradle.*
import org.gradle.kotlin.dsl.provider.gradleKotlinDslOf

plugins {
  java
}

buildscript{
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
    mavenCentral()
  }

  dependencies {
    classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.12")
  }
}

apply(plugin="com.google.protobuf")

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


dependencies{
    implementation("com.google.protobuf:protobuf-java:3.0.0")
    implementation("io.grpc:grpc-protobuf:1.29.0")
    implementation("io.grpc:grpc-stub:1.29.0")
    implementation("io.grpc:grpc-netty-shaded:1.29.0")
    implementation("org.apache.tomcat:annotations-api:6.0.53")
}
