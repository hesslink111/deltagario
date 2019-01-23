package gamestate

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoBuf
import resources.Color

@Serializable
sealed class Action

@Serializable
data class CreatePlayer(val id: Long, val name: String, val color: Color, val size: Float, val position: Pair<Float, Float>): Action()
@Serializable
data class MovePlayer(val id: Long, val position: Pair<Float, Float>): Action()
@Serializable
data class ResizePlayer(val id: Long, val size: Float): Action()
@Serializable
data class DeletePlayer(val id: Long): Action()
@Serializable
data class CreateFood(val id: Long, val color: Color, val size: Float, val position: Pair<Float, Float>): Action()
@Serializable
data class DeleteFood(val id: Long): Action()

@Serializable
data class SetPlayerDirection(val direction: Pair<Float, Float>): Action()

class Message(val type: Int, val bytes: ByteArray) {
    companion object {
        fun fromByteArray(bytes: ByteArray) = Message(bytes[0].toInt(), bytes.sliceArray(1 until bytes.size))
    }

    fun toByteArray() = byteArrayOf(type.toByte()) + bytes
}

fun Action.toMessage(): Message = when(this) {
    // From server only
    is CreatePlayer -> Message(0, ProtoBuf.dump(CreatePlayer.serializer(), this))
    is MovePlayer -> Message(1, ProtoBuf.dump(MovePlayer.serializer(), this))
    is ResizePlayer -> Message(2, ProtoBuf.dump(ResizePlayer.serializer(), this))
    is DeletePlayer -> Message(3, ProtoBuf.dump(DeletePlayer.serializer(), this))
    is CreateFood -> Message(4, ProtoBuf.dump(CreateFood.serializer(), this))
    is DeleteFood -> Message(5, ProtoBuf.dump(DeleteFood.serializer(), this))

    // From player
    is SetPlayerDirection -> Message(6, ProtoBuf.dump(SetPlayerDirection.serializer(), this))
}

fun Message.toAction(): Action = when(type) {
    0 -> ProtoBuf.load(CreatePlayer.serializer(), bytes)
    1 -> ProtoBuf.load(MovePlayer.serializer(), bytes)
    2 -> ProtoBuf.load(ResizePlayer.serializer(), bytes)
    3 -> ProtoBuf.load(DeletePlayer.serializer(), bytes)
    4 -> ProtoBuf.load(CreateFood.serializer(), bytes)
    5 -> ProtoBuf.load(DeleteFood.serializer(), bytes)
    6 -> ProtoBuf.load(SetPlayerDirection.serializer(), bytes)
    else -> throw IllegalArgumentException("Unknown message type: $type")
}
