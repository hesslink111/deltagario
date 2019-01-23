package client

import entities.Position
import gamestate.CreateFood
import gamestate.DeleteFood
import gamestate.GameStateServer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import resources.Color

class FoodClient(
    private val gameState: GameStateServer
): CoroutineScope {

    override val coroutineContext = Job()
    private val disposables = CompositeDisposable()

    init {

        launch {
            // TODO: Create 10000 food
            for(i in 0 until 10000) {
                val action = CreateFood(gameState.generateId(), Color.random(), 50f, Position.random())
                gameState.submitAction(action)
            }
        }

        disposables.add(gameState
            .stateObservable
            .filter { it is DeleteFood }
            .subscribeOn(Schedulers.computation())
            .subscribe { launch {
                // Create a new food every time one is removed
                val action = CreateFood(gameState.generateId(), Color.random(), 50f, Position.random())
                gameState.submitAction(action)
            } })
    }
}