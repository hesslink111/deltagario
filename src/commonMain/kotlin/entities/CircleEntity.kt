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