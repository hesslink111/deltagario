package resources

data class Color(
    val r: Int,
    val g: Int,
    val b: Int
) {
    companion object {
        val Red = Color(255, 0, 0)
        val Green = Color(0, 255, 0)
        val Blue = Color(0, 0, 255)
    }
}
