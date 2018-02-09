package dk.eboks.app.domain.models

/**
 * Created by bison on 09-02-2018.
 */
data class AppState (
    var currentFolder : Folder? = null,
    var currentMessage : Message? = null
)
