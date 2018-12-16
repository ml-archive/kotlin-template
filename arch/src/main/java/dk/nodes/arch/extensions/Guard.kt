package dk.nodes.arch.extensions

/**
 * Created by joso (merged by bison) on 21-02-2018.
 */
// Declare an extension function that calls a lambda called block if the value is null
inline fun <T> T.guard(block: T.() -> Unit): T {
    if (this == null) block(); return this
}