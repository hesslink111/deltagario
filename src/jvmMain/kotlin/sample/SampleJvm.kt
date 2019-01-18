package sample

actual class Sample {
    actual fun checkMe() = 42
}

actual object Platform {
    actual val name: String = "JVM"
}

actual fun nativeMain(args: Array<String>) {
    // Figure out what to do here later
}