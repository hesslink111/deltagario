package renderer

import kotlinx.css.vh
import kotlinx.css.vw
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import state.GameStateClient
import styled.css
import styled.styledSvg

class RenderSurface: RComponent<RenderSurface.Props, RState>() {

    interface Props: RProps {
        var gameState: GameStateClient
    }

    override fun RBuilder.render() {
        styledSvg {
            css {
                width = 100.vw
                height = 100.vh
            }

            renderer(props.gameState)
        }
    }
}

fun RBuilder.renderSurface(gameState: GameStateClient) = child(RenderSurface::class) {
    attrs.gameState = gameState
}