package connection

import gamestate.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import resources.Color

class ClientConnection(
    private val gameState: GameStateServer,
    private val connection: WebSocket
) {

    private val disposables = CompositeDisposable()
    private var clientId: Long = 0L

    fun onOpen(handshake: ClientHandshake) {
        clientId = (0L..Long.MAX_VALUE).random()
        gameState.submitAction(CreatePlayer(clientId, "wally", Color.Blue, 10000f, Pair(100f, 100f)))
        disposables.add(gameState
            .stateObservable
            .observeOn(Schedulers.io())
            .subscribe { action ->
                connection.send(action.toMessage().toByteArray())
            })
        gameState.submitAction(MovePlayer(clientId, Pair(150f, 150f)))
    }

    fun onMessage(message: String) {

    }

    fun onError(ex: Exception) {
        ex.printStackTrace()
    }

    fun onClose(code: Int, reason: String, remote: Boolean) {
        disposables.clear()
        gameState.submitAction(DeletePlayer(clientId))
    }
}
