package entities

import resources.Color

data class Food(
    override val id: Long,
    override var position: Pair<Float, Float>,
    override var size: Float,
    override var color: Color
): CircleEntity