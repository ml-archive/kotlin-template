package dk.eboks.app.domain.managers


/**
 * Created by bison on 16-02-2018.
 */
interface UIManager {
    fun showMessageScreen()
    fun showEmbeddedMessageScreen()
    fun showMessageLockedScreen()
    fun showMessagePromulgatedScreen()
    fun showMessageConfirmOpenScreen()
    fun showMessageReceiptOpenScreen()

    fun showFolderContentScreen()
}