package renderer.manual

import entities.CircleEntity
import entities.Player
import kotlinx.css.Color
import org.w3c.dom.svg.SVGCircleElement
import org.w3c.dom.svg.SVGElement
import org.w3c.dom.svg.SVGGElement
import renderer.SvgNameSpace
import resources.rgb
import kotlin.browser.document

class CircleElement(
    private val circleEntity: CircleEntity
) {

    private val circle: SVGCircleElement = document.createElementNS(SvgNameSpace, "circle") as SVGCircleElement
    private val text = document.createElementNS(SvgNameSpace, "text")

    init {
        text.setAttribute("text-anchor", "middle")
        text.setAttribute("allignment-baseline", "middle")
        text.setAttribute("fill", Color.white.value)
        update()
    }

    fun update() {
        val (x, y) = circleEntity.position
        circle.suspendRedraw {
            circle.setAttribute("cx", x.toString())
            circle.setAttribute("cy", y.toString())
            circle.setAttribute("r", circleEntity.radius.toString())
            circle.setAttribute("fill", circleEntity.color.rgb.value)

            text.setAttribute("x", x.toString())
            text.setAttribute("y", y.toString())

            if(circleEntity is Player) {
                text.textContent = circleEntity.name
            }
        }
    }

    fun attach(g: SVGGElement) {
        g.appendChild(circle)
        g.appendChild(text)
    }

    fun detach(g: SVGGElement) {
        g.removeChild(circle)
        g.removeChild(text)
    }

}

inline fun SVGElement.suspendRedraw(crossinline block: () -> Unit) {
    val owner = ownerSVGElement

    if(owner != null) {
        val suspendHandle = owner.suspendRedraw(1000)
        block()
        owner.unsuspendRedraw(suspendHandle)
    } else {
        block()
    }
}