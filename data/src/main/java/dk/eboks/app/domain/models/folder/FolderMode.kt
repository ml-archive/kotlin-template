package dk.eboks.app.domain.models.folder

enum class FolderMode(val type: String) {
    NORMAL("normal"),
    SELECT("select"),
    SELECTFOLDER("selectFolder"),
    EDIT("email");

    override fun toString(): String {
        return when (this) {
            NORMAL -> "normal"
            SELECT -> "select"
            EDIT -> "edit"
            SELECTFOLDER -> "selectFolder"
            else -> "unknown"
        }
    }
}