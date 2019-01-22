package entities

import resources.Color

data class Food(
    override val id: Long,
    override var position: Pair<Double, Double>,
    override var size: Double,
    override var color: Color
): CircleEntity