package dk.nodes.template.presentation.ui.base

interface BaseChange {

    /**
     * Obfuscated string that only contains the class name and hash code (the latter to help
     * differentiate between repeated states).
     *
     * Useful for hiding sensitive information when logging states.
     */
    fun obfuscatedString() = "${javaClass.simpleName}@${hashCode()}"
}