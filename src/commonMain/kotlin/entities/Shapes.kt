package entities

interface Circle {
    var position: Pair<Float, Float>
    var radius: Float
}

interface Rect {
    // Note that this is the center position
    var position: Pair<Float, Float>
    var width: Float
    var height: Float
}

fun Rect.collidesWith(circle: Circle): Boolean {
    // Only checking rect-rect collision
    val (rcx, rcy) = position
    val l1x = rcx - width / 2
    val r1x = rcx + width / 2
    val l1y = rcy + height / 2
    val r1y = rcy - height / 2
    val (ccx, ccy) = circle.position
    val l2x = ccx - circle.radius
    val r2x = ccx + circle.radius
    val l2y = ccy + circle.radius
    val r2y = ccy - circle.radius

    if(l1x > r2x || l2x > r1x) {
        return false
    }

    if(l1y < r2y || l2y < r1y) {
        return false
    }

    return true
}

operator fun Pair<Float, Float>.times(float: Float): Pair<Float, Float> = (first * float) to (second * float)

operator fun Pair<Float, Float>.plus(vector: Pair<Float, Float>) = (first + vector.first) to (second + vector.second)

class Position private constructor() {
    companion object {
        fun random(): Pair<Float, Float> {
            val x = (0 until 10000).random().toFloat()
            val y = (0 until 10000).random().toFloat()
            return x to y
        }
    }
}