package util

/**
 * I couldn't get any of the Optional libraries to work with KotlinJS, so this is
 * just going to be a basic implementation. This will closely follow gojuno's
 * KOptional. Actually it's an exact copy.
 *
 * https://github.com/gojuno/koptional/blob/master/koptional/src/main/kotlin/com/gojuno/koptional/Optional.kt
 */
sealed class Optional<out T: Any> {
    abstract fun toNullable(): T?
    abstract operator fun component1(): T?
}

data class Some<out T: Any>(val value: T): Optional<T>() {
    override fun toNullable(): T? = value
}

object None: Optional<Nothing>() {
    override fun toNullable(): Nothing? = null
    override fun component1(): Nothing? = null
}

fun <T : Any> T?.toOptional(): Optional<T> = if (this == null) None else Some(this)
