import connection.ServerConnection
import connection.serverConnector
import react.dom.render
import renderer.mouseEventDetector
import renderer.renderer
import state.GameStateClient
import kotlin.browser.document

actual object Platform {
    actual val name: String = "JS"
}

actual fun nativeMain(args: Array<String>) {

    val serverConnection = ServerConnection()
    val gameState = GameStateClient()

    render(document.getElementById("react-root")) {
        serverConnector(serverConnection, gameState)
        renderer(gameState)
        mouseEventDetector(serverConnection)
    }
}
