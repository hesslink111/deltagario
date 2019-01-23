package sample

import entities.Circle
import entities.CircleEntity
import entities.Rect
import entities.collidesWith
import gamestate.*
import kotlinx.serialization.protobuf.ProtoBuf
import resources.Color
import structures.QuadTree
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

    @Test
    fun collisions() {
        val rect = object: Rect {
            override var position = 1f to 1f
            override var width = 2f
            override var height = 2f
        }

        val circle1 = object: Circle {
            override var position = Pair(2f, 1f)
            override var radius = 1f
        }

        val circle2 = object: Circle {
            override var position = Pair(4f, 1f)
            override var radius = 1f
        }

        assertTrue(rect.collidesWith(circle1))
        assertFalse(rect.collidesWith(circle2))
    }

    @Test
    fun quadtrees() {
        val quadTree = QuadTree(Pair(500f, 500f), 1000f, 1000f)

        assertEquals(0, quadTree.size)
        assertEquals(0, quadTree.entities.size)
        assertEquals(0, quadTree.subtrees.size)

        val circle1 = object: CircleEntity {
            override val id = 0L
            override var size = 0f
            override var color = Color.Red
            override var position = Pair(2f, 2f)
            override var radius = 1f
        }

        quadTree += circle1

        assertEquals(1, quadTree.size)
        assertEquals(1, quadTree.entities.size)
        assertEquals(0, quadTree.subtrees.size)

        quadTree += circle1

        assertEquals(1, quadTree.size)
        assertEquals(1, quadTree.entities.size)
        assertEquals(0, quadTree.subtrees.size)

        val circle2 = object: CircleEntity {
            override val id = 0L
            override var size = 0f
            override var color = Color.Red
            override var position = Pair(2f, 502f)
            override var radius = 1f
        }

        val circle3 = object: CircleEntity {
            override val id = 0L
            override var size = 0f
            override var color = Color.Red
            override var position = Pair(502f, 2f)
            override var radius = 1f
        }

        val circle4 = object: CircleEntity {
            override val id = 0L
            override var size = 0f
            override var color = Color.Red
            override var position = Pair(502f, 502f)
            override var radius = 1f
        }

        quadTree += circle2
        quadTree += circle3
        quadTree += circle4

        assertEquals(4, quadTree.size)
        assertEquals(4, quadTree.entities.size)
        assertEquals(0, quadTree.subtrees.size)

        val circle5 = object: CircleEntity {
            override val id = 0L
            override var size = 0f
            override var color = Color.Red
            override var position = Pair(4f, 4f)
            override var radius = 1f
        }

        quadTree += circle5

        assertEquals(5, quadTree.size)
        assertEquals(0, quadTree.entities.size)
        assertEquals(4, quadTree.subtrees.size)
        assertEquals(5, quadTree.countTreesRecursive())
        assertEquals(5, quadTree.countSizeRecursively())

        quadTree -= circle1

        assertEquals(4, quadTree.size)
        assertEquals(4, quadTree.entities.size)
        assertEquals(0, quadTree.subtrees.size)
    }

    private fun QuadTree.countTreesRecursive(): Int {
        return 1 + subtrees.sumBy { it.countTreesRecursive() }
    }

    private fun QuadTree.countSizeRecursively(): Int {
        return entities.size + subtrees.sumBy { it.countSizeRecursively() }
    }
}