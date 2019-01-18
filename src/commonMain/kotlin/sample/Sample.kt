package sample

expect class Sample() {
    fun checkMe(): Int
}

expect object Platform {
    val name: String
}

expect fun nativeMain(args: Array<String>)

fun hello(): String = "Hello from ${Platform.name}"

fun main(args: Array<String>) {
    println(hello())
    nativeMain(args)
}