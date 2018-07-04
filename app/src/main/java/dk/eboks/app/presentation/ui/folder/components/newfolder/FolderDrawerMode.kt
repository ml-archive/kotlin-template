package dk.eboks.app.presentation.ui.folder.components.newfolder

enum class FolderDrawerMode(val type : String) {
    NEW("new"),
    EDIT("email");


    override fun toString(): String {
        when(this)
        {
            NEW -> return "new"
            EDIT -> return "edit"
            else -> return "unknown"
        }
    }
}