package connection

import gamestate.toAction
import kodando.rxjs.Subscription
import kodando.rxjs.subscribeNext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import state.GameStateClient
import kotlin.coroutines.CoroutineContext

class ServerConnector: RComponent<ServerConnector.Props, RState>(), CoroutineScope {

    interface Props: RProps {
        var serverConnection: ServerConnection
        var gameState: GameStateClient
    }

    private val subscription = Subscription {}
    override val coroutineContext = Job()

    override fun componentDidMount() {
        subscription.add(props.serverConnection
            .socketStateObservable
            .subscribeNext { state ->
                when(state) {
                    SocketStates.Open -> {}
                    SocketStates.Opening -> {}
                    SocketStates.Closed -> launch { reconnectDelayed() }
                }
            })

        subscription.add(props.serverConnection
            .socketMessageObservable
            .subscribeNext { message ->
                val action = message.toAction()
                println("Got action from server: $action")
                launch { props.gameState.submitAction(action) }
            })
    }

    private suspend fun reconnectDelayed() {
        println("Will reconnect after 10 seconds")
        delay(10000)

        // Nobody else has tried to open the socket
        if(props.serverConnection.socketState == SocketStates.Closed) {
            println("Reconnecting...")
            props.serverConnection.connect()
        }
    }

    override fun componentWillUnmount() {
        subscription.unsubscribe()
        coroutineContext.cancel()
    }

    override fun RBuilder.render() {}
}

inline fun RBuilder.serverConnector(
    serverConnection: ServerConnection,
    gameState: GameStateClient
) = child(ServerConnector::class) {
    attrs.serverConnection = serverConnection
    attrs.gameState = gameState
}