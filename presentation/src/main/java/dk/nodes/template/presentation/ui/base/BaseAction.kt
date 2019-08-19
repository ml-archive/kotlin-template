package dk.nodes.template.presentation.ui.base

interface BaseAction {
    /**
     * Obfuscated string that only contains the class name and hash code (the latter to help
     * differentiate between repeated actions).
     *
     * Useful for hiding sensitive information when logging actions.
     */
    fun obfuscatedString() = "${javaClass.simpleName}@${hashCode()}"
}