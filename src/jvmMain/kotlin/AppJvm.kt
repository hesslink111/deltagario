import client.ConnectedClient
import client.FoodClient
import gamestate.GameStateServer
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.nio.ByteBuffer

actual object Platform {
    actual val name: String = "JVM"
}

actual fun nativeMain(args: Array<String>) {
    val gameState = GameStateServer()

    val clients: MutableMap<WebSocket, ConnectedClient> = mutableMapOf()

    // Psuedoclients
    val foodClient = FoodClient(gameState)

    // Figure out what to do here later
    val server = object: WebSocketServer() {

        override fun onStart() {
            println("Socket server started")
        }

        override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
            val clientConnection = ConnectedClient(gameState, conn)
            clients += conn to clientConnection
            clientConnection.onOpen(handshake)
        }

        override fun onMessage(conn: WebSocket, message: String) {
            // Do nothing with strings
        }

        override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
            if(message != null) {
                val bytes = ByteArray(message.remaining()) { i -> message[i] }
                clients[conn]?.onMessage(bytes)
            }
        }

        override fun onError(conn: WebSocket?, ex: Exception) {
            if(conn != null) {
                clients[conn]?.onError(ex)
            } else {
                ex.printStackTrace()
            }
        }

        override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
            clients[conn]?.onClose(code, reason, remote)
            clients -= conn
        }
    }

    server.run()
}