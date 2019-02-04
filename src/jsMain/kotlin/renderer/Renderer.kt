package renderer

import gamestate.*
import kodando.rxjs.*
import kodando.rxjs.operators.filter
import kodando.rxjs.operators.map
import kodando.rxjs.operators.switchMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.w3c.dom.svg.SVGGElement
import react.*
import renderer.manual.CircleElement
import state.GameStateClient
import util.None
import util.Optional
import util.Some
import kotlin.browser.window

const val NameSpace = "http://www.w3.org/2000/svg"

class Renderer: RComponent<Renderer.Props, RState>(), CoroutineScope {

    interface Props: RProps {
        var gameState: GameStateClient
    }

    private lateinit var job: Job
    override val coroutineContext get() = job

    private val subscription = Subscription {}
    private var svgGroupRelay = BehaviorSubject<Optional<SVGGElement>>(None)

    private val elements: MutableMap<Long, CircleElement> = mutableMapOf()

    override fun componentWillMount() {
        job = Job()

        // Still need to make sure we get all the init events
        subscription.add(svgGroupRelay
            .filter { optional -> optional is Some }
            .switchMap { (group) -> props
                .gameState
                .stateChangeObservable
                .map { action -> Pair(group!!, action) }
            }
            .subscribeNext { (g, action) ->
                when(action) {
                    is CreatePlayer -> {
                        val player = props.gameState.players[action.id] ?: return@subscribeNext
                        val playerElement = CircleElement(player)
                        playerElement.attach(g)
                        elements[action.id] = playerElement
                    }
                    is MovePlayer -> elements[action.id]?.update()
                    is ResizePlayer -> elements[action.id]?.update()
                    is DeletePlayer -> {
                        elements[action.id]?.detach(g)
                        elements -= action.id
                    }
                    is CreateFood -> {
                        val food = props.gameState.foods[action.id] ?: return@subscribeNext
                        val foodElement = CircleElement(food)
                        foodElement.attach(g)
                        elements[action.id] = foodElement
                    }
                    is DeleteFood -> {
                        elements[action.id]?.detach(g)
                        elements -= action.id
                    }
                }

                // Handle clientplayer
                val clientPlayer = props.gameState.players[props.gameState.clientPlayerId]
                val (x, y) = clientPlayer?.position ?: Pair(500f, 500f)
                val transformX = -x + window.innerWidth / 2
                val transformY = -y + window.innerHeight / 2
                g.setAttribute("transform", "translate($transformX, $transformY)")
            })
    }

    override fun shouldComponentUpdate(nextProps: Props, nextState: RState): Boolean {
        return false
    }

    override fun componentWillUnmount() {
        subscription.unsubscribe()
        coroutineContext.cancel()
    }

    override fun RBuilder.render() {
        g {
            ref { g: SVGGElement ->
                svgGroupRelay.next(Some(g))
            }
        }
    }
}

inline fun RBuilder.renderer(gameState: GameStateClient) = child(Renderer::class) {
    attrs.gameState = gameState
}
