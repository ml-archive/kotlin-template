package dk.eboks.app.mail.presentation.ui.folder.components

enum class FolderDrawerMode(val type: String) {
    NEW("new"),
    EDIT("email");

    override fun toString(): String {
        return when (this) {
            NEW -> "new"
            EDIT -> "edit"
            else -> "unknown"
        }
    }
}