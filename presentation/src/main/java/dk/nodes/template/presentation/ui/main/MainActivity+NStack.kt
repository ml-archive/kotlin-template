package dk.nodes.template.presentation.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.AppUpdateState
import dk.nodes.nstack.kotlin.models.Message
import dk.nodes.nstack.kotlin.models.RateReminder
import dk.nodes.nstack.kotlin.models.Result
import dk.nodes.nstack.kotlin.models.state
import dk.nodes.nstack.kotlin.models.update
import dk.nodes.template.presentation.nstack.Translation
import kotlinx.coroutines.launch

fun MainActivity.setupNStack() {
    lifecycleScope.launch {
        when (val result = NStack.appOpen()) {
            is Result.Success -> {
                val appUpdateManager = AppUpdateManagerFactory.create(this@setupNStack)

                appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->

                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                    ) {   //  check for the type of update flow you want
                        when (result.value.data.update.state) {
                            AppUpdateState.NONE -> { /* Nothing to do */
                            }
                            AppUpdateState.UPDATE -> showUpdateDialog(result.value.data.update, appUpdateInfo)
                            AppUpdateState.FORCE -> showForceDialog(result.value.data.update, appUpdateInfo)
                            AppUpdateState.CHANGELOG -> showChangelogDialog(result.value.data.update, appUpdateInfo)
                        }
                    }

                result.value.data.message?.let { showMessageDialog(it) }
                }
                result.value.data.rateReminder?.let { showRateReminderDialog(it) }
            }
            is Result.Error -> {
            }
        }
    }
}

fun MainActivity.showRateReminderDialog(rateReminder: RateReminder) {
    AlertDialog.Builder(this)
        .setMessage(rateReminder.body)
        .setTitle(rateReminder.title)
        .setCancelable(false)
        .setPositiveButton(rateReminder.yesButton) { dialog, _ ->
            NStack.onRateReminderAction(true)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(rateReminder.link)))
            dialog.dismiss()
        }
        .setNegativeButton(rateReminder.noButton) { dialog, _ ->
            NStack.onRateReminderAction(false)
            dialog.dismiss()
        }
        .setNeutralButton(rateReminder.laterButton) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

fun MainActivity.showMessageDialog(message: Message) {
    AlertDialog.Builder(this)
        .setMessage(message.message)
        .setCancelable(false)
        .setPositiveButton(Translation.defaultSection.ok) { dialog, _ ->
            NStack.messageSeen(message)
            dialog.dismiss()
        }
        .show()
}

fun MainActivity.showUpdateDialog(
    appUpdate: AppUpdate,
    appUpdateInfo: AppUpdateInfo
) {

    AlertDialog.Builder(this)
        .setTitle(appUpdate.update?.translate?.title ?: return)
        .setMessage(appUpdate.update?.translate?.message ?: return)
        .setPositiveButton(appUpdate.update?.translate?.positiveButton) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

fun MainActivity.showChangelogDialog(
    appUpdate: AppUpdate,
    appUpdateInfo: AppUpdateInfo
) {
    AlertDialog.Builder(this)
        .setTitle(appUpdate.update?.translate?.title ?: return)
        .setMessage(appUpdate.update?.translate?.message ?: return)
        .setNegativeButton(appUpdate.update?.translate?.negativeButton ?: return) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

fun MainActivity.startPlayStore() {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
        )
    } catch (anfe: android.content.ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

fun MainActivity.showForceDialog(
    appUpdate: AppUpdate,
    appUpdateInfo: AppUpdateInfo
) {
    val dialog = AlertDialog.Builder(this)
        .setTitle(appUpdate.update?.translate?.title ?: return)
        .setMessage(appUpdate.update?.translate?.message ?: return)
        .setCancelable(false)
        .setPositiveButton(appUpdate.update?.translate?.positiveButton, null)
        .create()

    dialog.setOnShowListener {
        val b = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        b.setOnClickListener {
            startPlayStore()
        }
    }

    dialog.show()
}
