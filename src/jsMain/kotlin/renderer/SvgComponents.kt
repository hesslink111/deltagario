package renderer

import kotlinx.html.HTMLTag
import react.RBuilder
import react.dom.RDOMBuilder
import react.dom.tag

inline fun RBuilder.circle(block: RDOMBuilder<HTMLTag>.() -> Unit) {
    tag(block) { consumer -> HTMLTag(
        "circle",
        consumer,
        emptyMap(),
        null,
        true,
        false
    ) }
}

inline fun RBuilder.text(block: RDOMBuilder<HTMLTag>.() -> Unit) {
    tag(block) { consumer -> HTMLTag(
        "text",
        consumer,
        emptyMap(),
        null,
        true,
        false
    ) }
}

inline fun RBuilder.g(block: RDOMBuilder<HTMLTag>.() -> Unit) {
    tag(block) { consumer -> HTMLTag(
        "g",
        consumer,
        emptyMap(),
        null,
        true,
        false
    ) }
}