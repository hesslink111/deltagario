package sample

import gamestate.*
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SampleTests {
    @Test
    fun protobuf() {
        val movePlayerOriginal = MovePlayer(0L, Pair(28f, 29f))
        val movePlayerMsgOriginal = movePlayerOriginal.toMessage()
        val bytes = movePlayerMsgOriginal.toByteArray()
        val movePlayerMsgNew = Message.fromByteArray(bytes)
        val movePlayerDeserialized = movePlayerMsgNew.toAction()
        assertEquals(movePlayerOriginal, movePlayerDeserialized)
    }
}