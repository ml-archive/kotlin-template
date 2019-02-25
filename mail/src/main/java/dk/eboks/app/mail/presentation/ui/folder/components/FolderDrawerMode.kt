package dk.eboks.app.mail.presentation.ui.folder.components

enum class FolderDrawerMode(val type: String) {
    NEW("new"),
    EDIT("email");

    override fun toString(): String {
        when (this) {
            NEW -> return "new"
            EDIT -> return "edit"
            else -> return "unknown"
        }
    }
}