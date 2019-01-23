package resources

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val r: Short,
    val g: Short,
    val b: Short
) {

    companion object {
        val Red = Color(255, 0, 0)
        val Green = Color(0, 255, 0)
        val Blue = Color(0, 0, 255)

        fun random(): Color {
            val r = (0..255).random().toShort()
            val g = (0..255).random().toShort()
            val b = (0..255).random().toShort()
            return Color(r, g, b)
        }
    }
}
