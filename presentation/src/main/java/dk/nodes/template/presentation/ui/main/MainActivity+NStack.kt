package dk.nodes.template.presentation.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.AppUpdateState
import dk.nodes.nstack.kotlin.models.Message
import dk.nodes.nstack.kotlin.models.RateReminder
import dk.nodes.template.presentation.nstack.Translation
import timber.log.Timber

fun MainActivity.setupNStack() {
    NStack.onAppUpdateListener = { appUpdate ->
        when (appUpdate.update.state) {
            AppUpdateState.NONE -> {
            }
            AppUpdateState.UPDATE -> {
                showUpdateDialog(appUpdate.update)
            }
            AppUpdateState.FORCE -> {
                showForceDialog(appUpdate.update)
            }
            AppUpdateState.CHANGELOG -> {
                showChangelogDialog(appUpdate.update)
            }
        }
        appUpdate.message?.let { showMessageDialog(it) }
        appUpdate.rateReminder?.let { showRateReminderDialog(it) }
    }
    NStack.appOpen { success ->
        Timber.e("appopen success = $success")
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

fun MainActivity.showUpdateDialog(appUpdate: AppUpdate) {
    AlertDialog.Builder(this)
        .setTitle(appUpdate.update?.translate?.title ?: return)
        .setMessage(appUpdate.update?.translate?.message ?: return)
        .setPositiveButton(appUpdate.update?.translate?.positiveButton) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

fun MainActivity.showChangelogDialog(appUpdate: AppUpdate) {
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

fun MainActivity.showForceDialog(appUpdate: AppUpdate) {
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
