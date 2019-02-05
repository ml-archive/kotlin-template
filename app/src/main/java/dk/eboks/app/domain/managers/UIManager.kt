package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.folder.Folder

/**
 * Created by bison on 16-02-2018.
 */
interface UIManager {
    fun showLoginScreen()
    fun showMessageScreen()
    fun showEmbeddedMessageScreen()
    fun showMessageOpeningScreen()
    fun showFolderContentScreen(folder: Folder)
    fun showPermissionRequestScreen()
}