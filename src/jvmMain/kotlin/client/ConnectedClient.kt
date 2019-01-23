package client

import entities.plus
import entities.times
import gamestate.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import resources.Color
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureNanoTime

class ConnectedClient(
    private val gameState: GameStateServer,
    private val connection: WebSocket
): CoroutineScope {

    private val disposables = CompositeDisposable()
    private var clientId: Long = 0L

    // Actions
    private var playerDirection: SetPlayerDirection? = null

    override val coroutineContext: CoroutineContext = Job()

    fun onOpen(handshake: ClientHandshake) {
        clientId = (0L..Long.MAX_VALUE).random()
        launch { gameState.submitAction(CreatePlayer(clientId, "wally", Color.Blue, 10000f, Pair(100f, 100f))) }
        disposables.add(gameState
            .stateObservable
            .observeOn(Schedulers.io())
            .subscribe { action ->
                connection.send(action.toMessage().toByteArray())
            })
        launch { gameState.submitAction(MovePlayer(clientId, Pair(150f, 150f))) }
        launch { processPlayerActions() }
    }

    fun onMessage(bytes: ByteArray) {
        val message = Message.fromByteArray(bytes)
        val action = message.toAction()

        if(action is SetPlayerDirection) {
            playerDirection = action
        }
    }

    private suspend fun processPlayerActions() {
        while(isActive) {
            val delta = measureNanoTime { delay(33) } / 1000000f

            playerDirection?.let { action ->
                val player = gameState.players[clientId] ?: return@let
                val direction = action.direction
                val vector = direction * (delta / player.size)
                val updateAction = MovePlayer(player.id, player.position + vector)
                gameState.submitAction(updateAction)
            }
        }
    }

    fun onError(ex: Exception) {
        ex.printStackTrace()
    }

    fun onClose(code: Int, reason: String, remote: Boolean) {
        disposables.clear()
        // Global scope because this scope is about to be killed
        // It's unclear to me whether this is guaranteed to be executed after the CreatePlayer action.
        // I guess we could await that first job.
        GlobalScope.launch {
            gameState.submitAction(DeletePlayer(clientId))
        }
        coroutineContext.cancel()
    }
}
