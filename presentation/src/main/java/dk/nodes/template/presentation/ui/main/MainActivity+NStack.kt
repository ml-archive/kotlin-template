package dk.nodes.template.presentation.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.AppUpdateState
import dk.nodes.template.presentation.nstack.Translation
import timber.log.Timber

fun MainActivity.setupNstack() {
    NStack.onAppUpdateListener = { appUpdate ->
        when (appUpdate.state) {
            AppUpdateState.NONE -> {
            }
            AppUpdateState.UPDATE -> {
                showUpdateDialog(appUpdate)
            }
            AppUpdateState.FORCE -> {
                showForceDialog(appUpdate)
            }
            AppUpdateState.CHANGELOG -> {
                showChangelogDialog(appUpdate)
            }
        }
    }
    NStack.appOpen { success ->
        Timber.e("appopen success = $success")
    }
}

fun MainActivity.showUpdateDialog(appUpdate: AppUpdate) {
    AlertDialog.Builder(this)
            .setTitle(appUpdate.title)
            .setMessage(appUpdate.message)
            .setPositiveButton(Translation.defaultSection.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
}

fun MainActivity.showChangelogDialog(appUpdate: AppUpdate) {
    AlertDialog.Builder(this)
            .setTitle(appUpdate.title)
            .setMessage(appUpdate.message)
            .setNegativeButton(appUpdate.negativeBtn) { dialog, _ ->
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
            .setTitle(appUpdate.title)
            .setMessage(appUpdate.message)
            .setCancelable(false)
            .setPositiveButton(Translation.defaultSection.ok, null)
            .create()

    dialog.setOnShowListener {
        val b = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        b.setOnClickListener {
            startPlayStore()
        }
    }

    dialog.show()
}
