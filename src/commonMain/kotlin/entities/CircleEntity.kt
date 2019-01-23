package entities

import resources.Color
import kotlin.math.PI
import kotlin.math.sqrt

interface CircleEntity {
    val id: Long
    var position: Pair<Float, Float>
    var size: Float
    var color: Color
}

val CircleEntity.radius get() = sqrt(size / PI)

operator fun Pair<Float, Float>.times(float: Float): Pair<Float, Float> = (first * float) to (second * float)

operator fun Pair<Float, Float>.plus(vector: Pair<Float, Float>) = (first + vector.first) to (second + vector.second)