package renderer

import kodando.rxjs.Subscription
import kodando.rxjs.subscribeNext
import kotlinx.css.vh
import kotlinx.css.vw
import react.*
import state.GameStateClient
import structures.QuadTree
import styled.css
import styled.styledSvg

class Renderer: RComponent<Renderer.Props, Renderer.State>() {

    interface Props: RProps {
        var gameState: GameStateClient
    }

    interface State: RState {
        var quadTree: QuadTree
    }

    private val subscription = Subscription {}

    override fun componentWillMount() {
        setState {
            quadTree = props.gameState.quadTree
        }
        subscription.add(props.gameState
            .stateChangeObservable
            .subscribeNext {
                setState {
                    quadTree = props.gameState.quadTree
                }
            })
    }

    override fun componentWillUnmount() {
        subscription.unsubscribe()
    }

    override fun RBuilder.render() {
        styledSvg {
            css {
                width = 100.vw
                height = 100.vh
            }

            quadTreeEntityRenderer(state.quadTree)
        }
    }
}

inline fun RBuilder.renderer(gameState: GameStateClient) = child(Renderer::class) {
    attrs.gameState = gameState
}
