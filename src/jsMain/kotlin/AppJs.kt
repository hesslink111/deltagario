import connection.serverConnection
import react.dom.render
import renderer.renderer
import state.createStateRx
import kotlin.browser.document

actual object Platform {
    actual val name: String = "JS"
}

val clientStateRx = createStateRx()

actual fun nativeMain(args: Array<String>) {
    render(document.getElementById("react-root")) {
        +"Hello World"
        serverConnection()
        renderer()
    }
}
