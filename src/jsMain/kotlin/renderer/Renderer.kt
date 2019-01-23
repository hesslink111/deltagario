package renderer

import entities.Food
import entities.Player
import entities.radius
import kodando.rxjs.Subscription
import kodando.rxjs.subscribeNext
import kotlinx.css.Color
import kotlinx.css.vh
import kotlinx.css.vw
import kotlinx.html.HTMLTag
import react.*
import react.dom.RDOMBuilder
import react.dom.tag
import resources.rgb
import state.GameStateClient
import styled.css
import styled.styledSvg

class Renderer: RComponent<Renderer.Props, Renderer.State>() {

    interface Props: RProps {
        var gameState: GameStateClient
    }

    interface State: RState {
        var players: List<Player>
        var food: List<Food>
    }

    private val subscription = Subscription {}

    override fun componentWillMount() {
        setState {
            players = emptyList()
            food = emptyList()
        }
        subscription.add(props.gameState
            .stateChangeObservable
            .subscribeNext {
                // Update this later
                val playersList = props.gameState.players.values.toList()
                val foodList = props.gameState.food.values.toList()
                setState {
                    players = playersList
                    food = foodList
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

            state.players.forEach { player ->
                val (x, y) = player.position
                circle {
                    attrs["cx"] = x
                    attrs["cy"] = y
                    attrs["r"] = player.radius
                    attrs["fill"] = player.color.rgb
                }
                text {
                    attrs["x"] = x
                    attrs["y"] = y
                    attrs["textAnchor"] = "middle"
                    attrs["alignmentBaseline"] = "middle"
                    attrs["fill"] = Color.white
                    +player.name
                }
            }

            state.food.forEach { food ->
                val (x, y) = food.position
                circle {
                    attrs["cx"] = x
                    attrs["cy"] = y
                    attrs["r"] = food.radius
                }
            }
        }
    }
}

inline fun RBuilder.renderer(gameState: GameStateClient) = child(Renderer::class) {
    attrs.gameState = gameState
}

inline fun RBuilder.circle(block: RDOMBuilder<HTMLTag>.() -> Unit) {
    tag(block) { consumer -> HTMLTag(
        "circle",
        consumer,
        emptyMap(),
        null,
        true,
        false
    ) }
}

inline fun RBuilder.text(block: RDOMBuilder<HTMLTag>.() -> Unit) {
    tag(block) { consumer -> HTMLTag(
        "text",
        consumer,
        emptyMap(),
        null,
        true,
        false
    ) }
}