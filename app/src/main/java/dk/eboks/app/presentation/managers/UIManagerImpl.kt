package dk.eboks.app.presentation.managers

import android.content.Context
import android.content.Intent
import android.os.Handler
import dk.eboks.app.App
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.presentation.ui.screens.mail.list.MailListActivity
import dk.eboks.app.presentation.ui.screens.message.MessageActivity
import dk.eboks.app.presentation.ui.screens.message.embedded.MessageEmbeddedActivity
import dk.eboks.app.system.managers.permission.PermissionRequestActivity
import dk.eboks.app.util.guard

/**
 * Created by bison on 16-02-2018.
 */
class UIManagerImpl(val context: Context) : UIManager {
    val handler by lazy {
        Handler(context.mainLooper)
    }

    override fun showMessageScreen() {
        handler.post {
            App.currentActivity()?.let { it.startActivity(Intent(context, MessageActivity::class.java)) }
                    .guard { context.startActivity(Intent(context, MessageActivity::class.java)) }
        }
    }

    override fun showEmbeddedMessageScreen() {
        handler.post {
            App.currentActivity()?.let { it.startActivity(Intent(context, MessageEmbeddedActivity::class.java)) }
                    .guard { context.startActivity(Intent(context, MessageEmbeddedActivity::class.java)) }
        }
    }

    override fun showMessageLockedScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessagePromulgatedScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessageConfirmOpenScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessageReceiptOpenScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showFolderContentScreen() {
        handler.post {
            App.currentActivity()?.let { it.startActivity(Intent(context, MailListActivity::class.java)) }
                    .guard { context.startActivity(Intent(context, MailListActivity::class.java)) }
        }
    }

    override fun showPermissionRequestScreen()
    {
        handler.post {
            App.currentActivity()?.let { it.startActivity(Intent(context, PermissionRequestActivity::class.java)) }
                    .guard { context.startActivity(Intent(context, PermissionRequestActivity::class.java)) }
        }
    }

}