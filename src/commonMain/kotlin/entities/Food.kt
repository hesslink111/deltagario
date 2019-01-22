package entities

import resources.Color

data class Food(
    override var position: Pair<Double, Double>,
    override var size: Double,
    override var color: Color
): CircleEntity