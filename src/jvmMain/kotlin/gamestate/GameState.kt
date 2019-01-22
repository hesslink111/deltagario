package gamestate

import entities.Food
import entities.Player
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import resources.Color
import java.util.concurrent.Executors

class GameState {

    val gameStateScheduler = Schedulers.from(Executors.newSingleThreadExecutor())

    val players: MutableList<Player> = mutableListOf()
    val food: MutableList<Food> = mutableListOf()

    sealed class Action {
        data class CreatePlayer(val id: Long, val name: String, val color: Color, val size: Double, val position: Pair<Double, Double>): Action()
        data class MovePlayer(val id: Long, val position: Pair<Double, Double>): Action()
        data class ResizePlayer(val id: Long, val size: Double): Action()
        data class DeletePlayer(val id: Long): Action()
        data class CreateFood(val id: Long, val color: Color, val size: Double, val position: Pair<Double, Double>): Action()
        data class DeleteFood(val id: Long): Action()
    }

    private val stateChangeSubject = PublishSubject.create<Action>()

    val stateObservable: Observable<Action> = Observable.create<Action> { emitter ->
        println("What thread is running this show: ${Thread.currentThread()}")
        println("^ Hopefully that says the game state thread")

        val playerStateChanges = players.map { player -> player.toCreationAction() }
        val foodStateChanges = food.map { food -> food.toCreationAction() }

        (playerStateChanges + foodStateChanges).forEach { action ->
            emitter.onNext(action)
        }

        emitter.setDisposable(stateChangeSubject
            .subscribe { action ->
                println("What thread is emitting this event?: ${Thread.currentThread()}")
                emitter.onNext(action)
            })
    }.subscribeOn(gameStateScheduler)

    fun submitAction(action: Action) {
        when(action) {
            is Action.CreatePlayer -> players.add(action.toPlayer())
            is Action.MovePlayer -> players.find { it.id == action.id }?.position = action.position
            is Action.ResizePlayer -> players.find { it.id == action.id }?.size = action.size
            is Action.DeletePlayer -> players.removeIf { it.id == action.id }
            is Action.CreateFood -> food.add(action.toFood())
            is Action.DeleteFood -> food.removeIf { it.id == action.id }
        }
        stateChangeSubject.onNext(action)
    }
}

fun Player.toCreationAction() = GameState.Action.CreatePlayer(id = id, color = color, size = size, position = position, name = name)
fun GameState.Action.CreatePlayer.toPlayer() = Player(id = id, color = color, size = size, position = position, name = name)
fun Food.toCreationAction() = GameState.Action.CreateFood(id = id, color = color, size = size, position = position)
fun GameState.Action.CreateFood.toFood() = Food(id = id, color = color, size = size, position = position)