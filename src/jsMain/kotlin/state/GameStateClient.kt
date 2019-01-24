package state

import gamestate.Action
import gamestate.GameState
import gamestate.ClientPlayer
import kodando.rxjs.*

class GameStateClient: GameState() {

    private val stateChangeSubject = Subject<Action>()

    var clientPlayerId: Long? = null

    override suspend fun submitAction(action: Action) {
        if(action is ClientPlayer) {
            clientPlayerId = action.id
        }

        super.submitAction(action)
        stateChangeSubject.next(action)
    }

    val stateChangeObservable = stateChangeSubject.asObservable()
}
