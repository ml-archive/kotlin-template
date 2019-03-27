package dk.nodes.template.util

inline fun <T> T.guard(block: T.() -> Unit): T {
    if (this == null) block(); return this
}