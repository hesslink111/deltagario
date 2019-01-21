package entities

data class Player(
    override var position: Pair<Double, Double>,
    override var size: Double
): CircleEntity