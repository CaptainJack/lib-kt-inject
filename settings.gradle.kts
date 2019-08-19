rootProject.name = "tool-depin"

include("tool-depin-gradle")
project(":tool-depin-gradle").projectDir = file("gradle-plugin")

enableFeaturePreview("GRADLE_METADATA")
