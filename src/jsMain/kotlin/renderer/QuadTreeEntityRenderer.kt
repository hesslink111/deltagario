package renderer

import entities.Food
import entities.Player
import kotlinx.css.Color
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import resources.rgb
import structures.QuadTree

class QuadTreeEntityRenderer: RComponent<QuadTreeEntityRenderer.Props, RState>() {

    interface Props: RProps {
        var quadTree: QuadTree
    }

    private var updateCounter: Long = 0L

    override fun shouldComponentUpdate(nextProps: Props, nextState: RState): Boolean {
        return (nextProps.quadTree.updateCounter != updateCounter)
            .also { updateCounter = nextProps.quadTree.updateCounter }
    }

    override fun RBuilder.render() {
        props.quadTree.entities.forEach { circleEntity ->
            if(circleEntity is Player) {
                val (x, y) = circleEntity.position
                circle {
                    attrs["cx"] = x
                    attrs["cy"] = y
                    attrs["r"] = circleEntity.radius
                    attrs["fill"] = circleEntity.color.rgb
                }
                text {
                    attrs["x"] = x
                    attrs["y"] = y
                    attrs["textAnchor"] = "middle"
                    attrs["alignmentBaseline"] = "middle"
                    attrs["fill"] = Color.white
                    +circleEntity.name
                }
            } else if(circleEntity is Food) {
                val (x, y) = circleEntity.position
                circle {
                    attrs["cx"] = x
                    attrs["cy"] = y
                    attrs["r"] = circleEntity.radius
                    attrs["fill"] = circleEntity.color.rgb
                }
            }
        }

        props.quadTree.subtrees.forEach { subtree ->
            quadTreeEntityRenderer(subtree)
        }
    }

}

fun RBuilder.quadTreeEntityRenderer(quadTree: QuadTree) = child(QuadTreeEntityRenderer::class) {
    attrs.quadTree = quadTree
}