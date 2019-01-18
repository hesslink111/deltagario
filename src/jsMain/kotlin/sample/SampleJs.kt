package sample

import react.dom.render
import kotlin.browser.document

actual class Sample {
    actual fun checkMe() = 12
}

actual object Platform {
    actual val name: String = "JS"
}

actual fun nativeMain(args: Array<String>) {
    render(document.getElementById("react-root")) {
        +"Hello World"
    }
}