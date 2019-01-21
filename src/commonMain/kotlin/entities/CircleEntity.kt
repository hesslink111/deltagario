package entities

import kotlin.math.PI
import kotlin.math.sqrt

interface CircleEntity {
    var position: Pair<Double, Double>
    var size: Double
}

val CircleEntity.radius get() = sqrt(size / PI)