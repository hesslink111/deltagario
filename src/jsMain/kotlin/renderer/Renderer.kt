package renderer

import clientStateRx
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
import state.ClientState
import styled.css
import styled.styledSvg

class Renderer: RComponent<RProps, Renderer.State>() {

    interface State: RState {
        var clientState: ClientState
    }

    private val subscription = Subscription {}

    override fun componentWillMount() {
        subscription.add(clientStateRx.subscribeNext { cState ->
            setState { clientState = cState }
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

            state.clientState.players.forEach { player ->
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
                    attrs["text-anchor"] = "middle"
                    attrs["alignment-baseline"] = "middle"
                    attrs["fill"] = Color.white
                    +player.name
                }
            }

            state.clientState.food.forEach { food ->
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

inline fun RBuilder.renderer() = child(Renderer::class) {}

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
    )
    }
}