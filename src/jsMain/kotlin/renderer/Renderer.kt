package renderer

import entities.Player
import kodando.rxjs.Subscription
import kodando.rxjs.operators.throttleTime
import kodando.rxjs.subscribeNext
import react.*
import state.GameStateClient
import structures.QuadTree
import kotlin.browser.window

class Renderer: RComponent<Renderer.Props, Renderer.State>() {

    interface Props: RProps {
        var gameState: GameStateClient
    }

    interface State: RState {
        var player: Player?
        var quadTree: QuadTree
    }

    private val subscription = Subscription {}

    override fun componentWillMount() {
        setState {
            player = null
            quadTree = props.gameState.quadTree
        }
        subscription.add(props.gameState
            .stateChangeObservable
            .throttleTime(33)
            .subscribeNext {
                setState {
                    player = props.gameState.players[props.gameState.clientPlayerId]
                    quadTree = props.gameState.quadTree
                }
            })
    }

    override fun componentWillUnmount() {
        subscription.unsubscribe()
    }

    override fun RBuilder.render() {
        g {
            val (x, y) = state.player?.position ?: Pair(500f, 500f)
            val transformX = -x + window.innerWidth / 2
            val transformY = -y + window.innerHeight / 2
            attrs["transform"] = "translate($transformX, $transformY)"

            quadTreeEntityRenderer(state.quadTree)
        }
    }
}

inline fun RBuilder.renderer(gameState: GameStateClient) = child(Renderer::class) {
    attrs.gameState = gameState
}
