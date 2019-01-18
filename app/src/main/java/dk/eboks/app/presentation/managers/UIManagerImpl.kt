package dk.eboks.app.presentation.managers

import android.content.Context
import android.content.Intent
import android.os.Handler
import dk.eboks.app.App
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.ui.mail.screens.list.MailListActivity
import dk.eboks.app.presentation.ui.message.screens.MessageActivity
import dk.eboks.app.presentation.ui.message.screens.embedded.MessageEmbeddedActivity
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.start.screens.StartActivity
import dk.eboks.app.system.managers.permission.PermissionRequestActivity
import dk.eboks.app.util.ActivityStarter
import dk.eboks.app.util.guard

/**
 * Created by bison on 16-02-2018.
 */
class UIManagerImpl(val context: Context) : UIManager {

    val handler by lazy {
        Handler(context.mainLooper)
    }

    // used when triggered from an authorization, but the boot should already have happened,
    // so it needs to be skipped (using the "noboot" extra). Otherwise, we'll end in a, infinite loop
    override fun showLoginScreen() {
        handler.post {
            App.currentActivity()?.let {
                ActivityStarter(it)
                        .activity(StartActivity::class.java)
                        .putExtra("noboot", true)
                        .putExtra("sessionExpired", true)
                        .start()
            }.guard {
                val i = Intent(context, StartActivity::class.java)
                i.putExtra("noboot", true)
                i.putExtra("sessionExpired", true)
                context.startActivity(i)
            }
        }
    }

    override fun showMessageScreen() {
        handler.post {
            App.currentActivity()?.let { it.startActivity(Intent(context, MessageActivity::class.java)); it.overridePendingTransition(0, 0) }
                    .guard { context.startActivity(Intent(context, MessageActivity::class.java)) }
        }
    }

    override fun showEmbeddedMessageScreen() {
        handler.post {
            App.currentActivity()?.let { it.startActivity(Intent(context, MessageEmbeddedActivity::class.java)); it.overridePendingTransition(0, 0) }
                    .guard { context.startActivity(Intent(context, MessageEmbeddedActivity::class.java)) }
        }
    }

    override fun showMessageOpeningScreen() {
        handler.post {
            App.currentActivity()?.startActivity(Intent(context, MessageOpeningActivity::class.java))
                    .guard { context.startActivity(Intent(context, MessageOpeningActivity::class.java)) }
        }
    }

    override fun showFolderContentScreen(folder: Folder) {
        val intent = Intent(context, MailListActivity::class.java)
        intent.putExtra("folder", folder)
        handler.post {
            App.currentActivity()?.let { it.startActivity(intent) }
                    .guard { context.startActivity(intent) }
        }
    }

    override fun showPermissionRequestScreen() {
        handler.post {
            App.currentActivity()?.startActivity(Intent(context, PermissionRequestActivity::class.java))
                    .guard { context.startActivity(Intent(context, PermissionRequestActivity::class.java)) }
        }
    }

}