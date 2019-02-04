package renderer.manual

import entities.CircleEntity
import entities.Player
import kotlinx.css.Color
import org.w3c.dom.svg.SVGGElement
import renderer.NameSpace
import resources.rgb
import kotlin.browser.document

class CircleElement(
    private val circleEntity: CircleEntity
) {

    private val circle = document.createElementNS(NameSpace, "circle")
    private val text = document.createElementNS(NameSpace, "text")

    init {
        text.setAttribute("text-anchor", "middle")
        text.setAttribute("allignment-baseline", "middle")
        text.setAttribute("fill", Color.white.value)
        update()
    }

    fun update() {
        val (x, y) = circleEntity.position
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

    fun attach(g: SVGGElement) {
        g.appendChild(circle)
    }

    fun detach(g: SVGGElement) {
        g.removeChild(g)
    }

}