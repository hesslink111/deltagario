package entities

import resources.Color

data class Player(
    override var position: Pair<Double, Double>,
    override var size: Double,
    override var color: Color,
    var name: String
): CircleEntity