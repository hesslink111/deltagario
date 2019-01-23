package state

import gamestate.Action
import gamestate.GameState
import kodando.rxjs.*

class GameStateClient: GameState() {

    private val stateChangeSubject = Subject<Unit>()

    override fun submitAction(action: Action) {
        super.submitAction(action)
        stateChangeSubject.next(Unit)
    }

    val stateChangeObservable = stateChangeSubject.asObservable()
}
