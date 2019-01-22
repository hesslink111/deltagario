import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

actual object Platform {
    actual val name: String = "JVM"
}

actual fun nativeMain(args: Array<String>) {
    // Figure out what to do here later
    val server = object: WebSocketServer() {
        override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
            println("Opened")
        }
        override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {}

        override fun onMessage(conn: WebSocket, message: String) {
            println("Hello")
        }

        override fun onStart() {}
        override fun onError(conn: WebSocket?, ex: Exception) {
            ex.printStackTrace()
        }

    }

    server.run()
}