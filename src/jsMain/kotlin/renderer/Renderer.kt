package renderer

import entities.Food
import entities.Player
import entities.radius
import kotlinx.css.pc
import kotlinx.css.pct
import kotlinx.css.vh
import kotlinx.css.vw
import kotlinx.html.HTMLTag
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.RDOMBuilder
import react.dom.svg
import react.dom.tag
import styled.css
import styled.styledSvg

class Renderer: RComponent<RProps, Renderer.State>() {

    interface State: RState {
        var players: List<Player>
        var food: List<Food>
    }

    init {
        state = object: State {
            override var players = listOf(Player(Pair(10.0, 10.1), 100.0))
            override var food = listOf(Food(Pair(20.0, 20.0), 20.0))
        }
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
                }
            }
        }
    }
}

inline fun RBuilder.renderer() = child(Renderer::class) {}

inline fun RBuilder.circle(block: RDOMBuilder<HTMLTag>.() -> Unit): Unit {
    tag(block) { consumer -> HTMLTag(
        "circle",
        consumer,
        emptyMap(),
        null,
        true,
        false
    ) }
}