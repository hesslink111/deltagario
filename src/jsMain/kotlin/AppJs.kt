import connection.serverConnection
import react.dom.render
import renderer.renderer
import state.GameStateClient
import kotlin.browser.document

actual object Platform {
    actual val name: String = "JS"
}

actual fun nativeMain(args: Array<String>) {

    val gameState = GameStateClient()

    render(document.getElementById("react-root")) {
        +"Hello World"
        serverConnection(gameState)
        renderer(gameState)
    }
}
