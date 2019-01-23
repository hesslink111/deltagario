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

class Position private constructor() {
    companion object {
        fun random(): Pair<Float, Float> {
            val x = (0 until 500).random().toFloat() // TODO: Update to 10000
            val y = (0 until 500).random().toFloat() // TODO: Update to 10000
            return x to y
        }
    }
}
