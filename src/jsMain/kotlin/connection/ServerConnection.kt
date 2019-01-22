package connection

import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState

class ServerConnection: RComponent<RProps, RState>() {

    lateinit var socket: WebSocket

    override fun componentDidMount() {
        socket = WebSocket("ws://localhost")

        socket.onopen = { event: Event ->
            println("Open on client")
        }

        socket.onmessage = { event: Event ->
            event as MessageEvent
            println("Event: ${event.data}")
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

fun RBuilder.serverConnection() = child(ServerConnection::class) {}