import connection.ClientConnection
import gamestate.GameStateServer
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

actual object Platform {
    actual val name: String = "JVM"
}

actual fun nativeMain(args: Array<String>) {
    val gameState = GameStateServer()

    val clients: MutableMap<WebSocket, ClientConnection> = mutableMapOf()

    // Figure out what to do here later
    val server = object: WebSocketServer() {

        override fun onStart() {
            println("Socket server started")
        }

        override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
            val clientConnection = ClientConnection(gameState, conn)
            clients += conn to clientConnection
            clientConnection.onOpen(handshake)
        }

        override fun onMessage(conn: WebSocket, message: String) {
            clients[conn]?.onMessage(message)
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