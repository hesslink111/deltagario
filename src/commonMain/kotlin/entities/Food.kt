package entities

data class Food(
    override var position: Pair<Double, Double>,
    override var size: Double
): CircleEntity