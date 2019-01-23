package connection

import gamestate.Message
import gamestate.toAction
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.ARRAYBUFFER
import org.w3c.dom.BinaryType
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import state.GameStateClient

class ServerConnection: RComponent<ServerConnection.Props, RState>() {

    interface Props: RProps {
        var gameState: GameStateClient
    }

    private lateinit var socket: WebSocket

    override fun componentDidMount() {
        socket = WebSocket("ws://localhost")
        socket.binaryType = BinaryType.ARRAYBUFFER

        socket.onopen = { event: Event ->
            println("Open on client")
        }

        socket.onmessage = { event: Event ->
            event as MessageEvent
            val data = Uint8Array(event.data as ArrayBuffer)
            val bytes = ByteArray(data.length) { i -> data[i] }
            val message = Message.fromByteArray(bytes)
            val action = message.toAction()
            println("Event: $action, length: ${bytes.size}")
            props.gameState.submitAction(action)
        }
    }

    override fun componentWillUnmount() {
        if(socket.readyState == WebSocket.OPEN || socket.readyState == WebSocket.CONNECTING) {
            socket.close()
        }
    }

    override fun RBuilder.render() {

    }
}

inline fun RBuilder.serverConnection(gameState: GameStateClient) = child(ServerConnection::class) {
    attrs.gameState = gameState
}