package entities

import resources.Color
import kotlin.math.PI
import kotlin.math.sqrt

interface CircleEntity: Circle {
    val id: Long
    override var position: Pair<Float, Float>
    var size: Float
    var color: Color
    override var radius
        get() = sqrt(size / PI.toFloat())
        set(value) {}

}
