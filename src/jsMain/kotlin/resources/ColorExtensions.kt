package resources

import kotlinx.css.rgb

inline val Color.rgb get() = rgb(r.toInt(), g.toInt(), b.toInt())