package renderer

import connection.ServerConnection
import gamestate.SetPlayerDirection
import gamestate.toMessage
import kodando.rxjs.Observable
import kodando.rxjs.Subscription
import kodando.rxjs.Unsubscribable
import kodando.rxjs.operators.map
import kodando.rxjs.operators.throttleTime
import kodando.rxjs.subscribeNext
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import kotlin.browser.window
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

class MouseEventDetector: RComponent<MouseEventDetector.Props, RState>() {

    interface Props: RProps {
        var serverConnection: ServerConnection
    }

    private val subscription = Subscription {}

    override fun componentDidMount() {
        subscription.add(MouseMoveObservable
            .mouseMoveRx
            .throttleTime(100)
            .map { (it.clientX - (window.innerWidth / 2.0)) to (it.clientY - (window.innerHeight / 2.0)) }
            .map { it.coerceIn(-100.0..100.0) }
            .map { (x, y) -> x.toFloat() to y.toFloat() }
            .subscribeNext { direction ->
                println("Mouse moved: $direction")
                props.serverConnection.sendMessage(SetPlayerDirection(direction).toMessage())
            })
    }

    override fun componentWillUnmount() {
        subscription.unsubscribe()
    }

    override fun RBuilder.render() {}
}

fun RBuilder.mouseEventDetector(serverConnection: ServerConnection) = child(MouseEventDetector::class) {
    attrs.serverConnection = serverConnection
}

class MouseMoveObservable {
    companion object {
        val mouseMoveRx = Observable<MouseEvent> {
            val mouseEventListener = { event: Event ->
                event as MouseEvent
                it.next(event)
            }
            window.addEventListener("mousemove", mouseEventListener)
            return@Observable object: Unsubscribable {
                override fun unsubscribe() {
                    window.removeEventListener("mousemove", mouseEventListener)
                }
            }
        }
    }
}

fun Pair<Double, Double>.coerceIn(range: ClosedRange<Double>): Pair<Double, Double> {
    val c = sqrt(first * first + second * second)

    return if (c in range && -c in range) {
        this
    } else {
        val smallest = min(abs(range.start), abs(range.endInclusive))
        first / c * smallest to second / c * smallest
    }
}
