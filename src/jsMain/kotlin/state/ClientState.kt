package state

import entities.Food
import entities.Player
import kodando.rxjs.*
import resources.Color

data class ClientState(
    val players: List<Player>,
    val food: List<Food>
)

fun createStateRx() = BehaviorSubject(ClientState(listOf(
    Player(-1L, Pair(100.0, 100.0), 2000.0, Color.Blue, "Bob"),
    Player(-2L, Pair(200.0, 150.0), 10000.0, Color.Red, "Jerry")
), listOf(
    Food(-3L, Pair(50.0, 100.0), 100.0, Color.Green)
)))
