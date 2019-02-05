package structures

import entities.CircleEntity
import entities.Rect
import entities.collidesWith
import entities.plus

class QuadTree(
    override var position: Pair<Float, Float>,
    override var width: Float,
    override var height: Float
): Rect {

    val entities: MutableSet<CircleEntity> = mutableSetOf()
    val subtrees: MutableList<QuadTree> = mutableListOf()
    var size = 0

    // This seems kind of sketchy
    var updateCounter = (Long.MIN_VALUE..Long.MAX_VALUE).random()

    operator fun plusAssign(entity: CircleEntity) {
        if(!this.collidesWith(entity)) {
            return
        }

        val objectAdded: Boolean

        if(subtrees.isEmpty()) {
            objectAdded = valueChanged({ entities.size }) {
                entities += entity
            }
        } else {
            objectAdded = valueChanged({ subtrees.sumBy { it.size } }) {
                subtrees.forEach { tree -> tree += entity }
            }
        }

        // We cannot implicitly guarantee something was added, same element may have existed already.
        if(objectAdded) {
            size++
            updateCounter++
        }

        if(size > 4 && subtrees.isEmpty()) {
            subtrees += QuadTree(position + Pair(width / 4, height / 4), width / 2, height / 2)
            subtrees += QuadTree(position + Pair(- width / 4, height / 4), width / 2, height / 2)
            subtrees += QuadTree(position + Pair(width / 4, - height / 4), width / 2, height / 2)
            subtrees += QuadTree(position + Pair(- width / 4, - height / 4), width / 2, height / 2)

            entities.forEach { e -> subtrees.forEach { tree -> tree += e } }
            entities.clear()
        }
    }

    operator fun minusAssign(entity: CircleEntity) {
        if(!this.collidesWith(entity)) {
            return
        }

        // We cannot implicitly guarantee something was removed (element may not have existed),
        // unless we keep track of every object at every root.
        val objectRemoved: Boolean

        if(subtrees.isEmpty()) {
            objectRemoved = valueChanged({ entities.size }) {
                entities -= entity
            }
        } else {
            objectRemoved = valueChanged({ subtrees.sumBy { it.size } }) {
                subtrees.forEach { tree -> tree -= entity }
            }
        }

        if(objectRemoved) {
            size--
            updateCounter++
        }

        if(size <= 4 && subtrees.isNotEmpty()) {
            val es = subtrees.map { it.entities }.flatten()
            entities.addAll(es)
            subtrees.clear()
        }
    }

    fun clear() {
        entities.clear()
        subtrees.clear()
        size = 0
        updateCounter++
    }
}

private inline fun valueChanged(crossinline valueGetter: () -> Int, crossinline block: () -> Unit): Boolean {
    val start = valueGetter()
    block()
    val end = valueGetter()
    return start != end
}