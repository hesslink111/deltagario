package gamestate

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class GameStateServer: GameState() {

    private val gameStateExecutor = Executors.newSingleThreadExecutor()
    private val gameStateScheduler = Schedulers.from(gameStateExecutor)
    private val gameStateDispatcher = gameStateExecutor.asCoroutineDispatcher()

    private val stateChangeSubject = PublishSubject.create<Action>()

    val stateObservable: Observable<Action> = Observable.create<Action> { emitter ->

        val playerStateChanges = players.values.map { player -> player.toCreationAction() }
        val foodStateChanges = food.values.map { food -> food.toCreationAction() }

        (playerStateChanges + foodStateChanges).forEach { action ->
            emitter.onNext(action)
        }

        emitter.setDisposable(stateChangeSubject
            .subscribe { action ->
                emitter.onNext(action)
            })
    }.subscribeOn(gameStateScheduler)

    private var nextId = 0L

    suspend fun generateId(): Long = withContext(gameStateDispatcher) { nextId++ }

    override suspend fun submitAction(action: Action) = withContext(gameStateDispatcher) {
        super.submitAction(action)
        stateChangeSubject.onNext(action)
    }
}
