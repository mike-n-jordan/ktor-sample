rootProject.name = "ktor-sample"

include(
    "application:app",
    "application:security",
    "features:user",
    "features:admin",
    "acceptance-tests",
)
