package gamestate

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executors

class GameStateServer: GameState() {

    private val gameStateScheduler = Schedulers.from(Executors.newSingleThreadExecutor())

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

    override fun submitAction(action: Action) {
        super.submitAction(action)
        stateChangeSubject.onNext(action)
    }
}
