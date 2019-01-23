package gamestate

import entities.Food
import entities.Player

open class GameState {

    val players: MutableMap<Long, Player> = mutableMapOf()
    val food: MutableMap<Long, Food> = mutableMapOf()

    open suspend fun submitAction(action: Action) {
        when(action) {
            is CreatePlayer -> players += action.id to action.toPlayer()
            is MovePlayer -> players[action.id]?.position = action.position
            is ResizePlayer -> players[action.id]?.size = action.size
            is DeletePlayer -> players -= action.id
            is CreateFood -> food += action.id to action.toFood()
            is DeleteFood -> food -= action.id
        }
    }
}

fun Player.toCreationAction() = CreatePlayer(id = id, color = color, size = size, position = position, name = name)
fun CreatePlayer.toPlayer() = Player(id = id, color = color, size = size, position = position, name = name)
fun Food.toCreationAction() = CreateFood(id = id, color = color, size = size, position = position)
fun CreateFood.toFood() = Food(id = id, color = color, size = size, position = position)
