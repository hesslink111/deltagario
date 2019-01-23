package connection

import gamestate.Message
import kodando.rxjs.BehaviorSubject
import kodando.rxjs.Subject
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.w3c.dom.ARRAYBUFFER
import org.w3c.dom.BinaryType
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event

class ServerConnection {

    private var socket: WebSocket? = null

    private val socketMessageSubject = Subject<Message>()
    val socketMessageObservable = socketMessageSubject.asObservable()

    private val socketStateSubject = BehaviorSubject(SocketStates.Closed)
    val socketStateObservable = socketStateSubject.asObservable()
    val socketState get() = socketStateSubject.value

    fun connect() {
        try {
            socket?.close()
        } catch(ex: Exception) {
            println("Exception while closing socket: $ex")
        }
        try {
            socket = WebSocket("ws://localhost")
        } catch(ex: Exception) {
            println("Exception while creating socket: $ex")
        }
        socket?.binaryType = BinaryType.ARRAYBUFFER
        socket?.onopen = ::onOpen
        socket?.onclose = ::onClose
        socket?.onerror = ::onError
        socket?.onmessage = ::onMessage
        socketStateSubject.next(SocketStates.Opening)
    }

    fun sendMessage(message: Message) {
        val bytes = message.toByteArray()
        val array = Uint8Array(bytes.size).apply { bytes.forEachIndexed { i, b -> this[i] = b } }
        try {
            socket?.send(array)
        } catch(ex: Exception) {
            println("Exception while sending message: $ex")
        }
    }

    private fun onOpen(event: Event) {
        println("Socket open")
        socketStateSubject.next(SocketStates.Open)
    }

    private fun onMessage(event: Event) {
        event as MessageEvent
        val array = Uint8Array(event.data as ArrayBuffer)
        val bytes = ByteArray(array.length) { i -> array[i] }
        val message = Message.fromByteArray(bytes)
        socketMessageSubject.next(message)
    }

    private fun onError(event: Event) {
        println("Socket error: $event")
    }

    private fun onClose(event: Event) {
        println("Socket closed")
        socket = null
        socketStateSubject.next(SocketStates.Closed)
    }
}

enum class SocketStates {
    Open,
    Opening,
    Closed
}
