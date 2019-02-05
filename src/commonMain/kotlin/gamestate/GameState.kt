package gamestate

import entities.Food
import entities.Player
import structures.QuadTree

open class GameState {

    val players: MutableMap<Long, Player> = mutableMapOf()
    val foods: MutableMap<Long, Food> = mutableMapOf()

    val quadTree = QuadTree(Pair(5000f, 5000f), 10000f, 10000f)

    fun currentStateActions(): List<Action> {
        val playerStateActions = players.values.map { player -> player.toCreationAction() }
        val foodStateActions = foods.values.map { food -> food.toCreationAction() }

        return playerStateActions + foodStateActions
    }

    open suspend fun submitAction(action: Action) {
        when(action) {
            is CreatePlayer -> {
                val player = action.toPlayer()
                players += action.id to player
                quadTree += player
            }
            is MovePlayer -> {
                val player = players[action.id] ?: return
                quadTree -= player
                player.position = action.position
                quadTree += player
            }
            is ResizePlayer -> {
                val player = players[action.id] ?: return
                quadTree -= player
                player.size = action.size
                quadTree += player
            }
            is DeletePlayer -> {
                val player = players[action.id] ?: return
                players -= action.id
                quadTree -= player
            }
            is CreateFood -> {
                val food = action.toFood()
                foods += action.id to food
                quadTree += food
            }
            is DeleteFood -> {
                val food = foods[action.id] ?: return
                foods -= action.id
                quadTree -= food
            }
            is ResetAll -> {
                foods.clear()
                players.clear()
                quadTree.clear()
            }
        }
    }
}

fun Player.toCreationAction() = CreatePlayer(id = id, color = color, size = size, position = position, name = name)
fun CreatePlayer.toPlayer() = Player(id = id, color = color, size = size, position = position, name = name)
fun Food.toCreationAction() = CreateFood(id = id, color = color, size = size, position = position)
fun CreateFood.toFood() = Food(id = id, color = color, size = size, position = position)
