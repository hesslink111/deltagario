package state

import gamestate.Action
import gamestate.GameState
import gamestate.ClientPlayer
import kodando.rxjs.*

class GameStateClient: GameState() {

    private val stateChangeSubject = Subject<Action>()

    var clientPlayerId: Long? = null

    val stateObservable: Observable<Action> = Observable<Action> { emitter ->
        currentStateActions().forEach { action ->
            emitter.next(action)
        }

        return@Observable stateChangeSubject.subscribe(emitter)
    }

    override suspend fun submitAction(action: Action) {
        if(action is ClientPlayer) {
            clientPlayerId = action.id
        }

        super.submitAction(action)
        stateChangeSubject.next(action)
    }
}
