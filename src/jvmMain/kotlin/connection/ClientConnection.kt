package connection

import entities.Player
import gamestate.GameState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import resources.Color

class ClientConnection(
    private val gameState: GameState,
    private val connection: WebSocket
) {

    private val disposables = CompositeDisposable()

    fun onOpen(handshake: ClientHandshake) {
        gameState.submitAction(GameState.Action.CreatePlayer(0L, "wally", Color.Blue, 10000.0, Pair(100.0, 100.0)))
        disposables.add(gameState
            .stateObservable
            .observeOn(Schedulers.io())
            .subscribe {
                println("Got action: $it")
                connection.send("action: $it")
            })
        gameState.submitAction(GameState.Action.MovePlayer(0L, Pair(150.0, 150.0)))
    }

    fun onMessage(message: String) {

    }

    fun onError(ex: Exception) {
        ex.printStackTrace()
    }

    fun onClose(code: Int, reason: String, remote: Boolean) {
        disposables.clear()
        gameState.submitAction(GameState.Action.DeletePlayer(0L))
    }
}
