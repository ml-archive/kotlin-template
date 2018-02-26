package dk.eboks.app.domain.managers


/**
 * Created by bison on 16-02-2018.
 */
interface UIManager {
    fun showMessageScreen()
    fun showEmbeddedMessageScreen()
    fun showMessageOpeningScreen()

    fun showFolderContentScreen()
    fun showPermissionRequestScreen()
}