package dk.eboks.app.presentation.ui.components.folder.folders

enum class FolderMode(val type: String) {
    NORMAL("normal"),
    SELECT("select"),
    SELECTFOLDER("selectFolder"),
    EDIT("email");


    override fun toString(): String {
        when (this) {
            NORMAL -> return "normal"
            SELECT -> return "select"
            EDIT -> return "edit"
            SELECTFOLDER -> return "selectFolder"
            else -> return "unknown"
        }
    }
}