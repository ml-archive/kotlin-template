package dk.nodes.template.presentation.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.util.Log
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.AppUpdateState
import dk.nodes.template.App
import dk.nodes.template.BuildConfig
import dk.nodes.template.R
import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.models.Translation
import dk.nodes.template.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.CrashManagerListener
import net.hockeyapp.android.UpdateManager
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {
    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNstack()
        setupHockey()
    }

    override fun onDestroy() {
        super.onDestroy()
        // If we checked for hockey updates, unregister
        UpdateManager.unregister()
    }

    private fun setupHockey() {
        if(BuildConfig.DEBUG) {
            // Auto-send crashes without asking user
            CrashManager.register(this, object : CrashManagerListener() {
                override fun shouldAutoUploadCrashes(): Boolean {
                    return true
                }
            })

            // Check for updates from Hockey
            UpdateManager.register(this)
        }

        // GDPR / Google's Personal/Sensitive policy dictates that we should ask the user
        // in user facing builds
        else {
            CrashManager.register(this)
        }
    }

    private fun setupNstack() {
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
            Log.e("debug", "appopen success = $success")
        }
    }

    private fun showUpdateDialog(appUpdate: AppUpdate) {
        AlertDialog.Builder(this)
                .setTitle(appUpdate.title)
                .setMessage(appUpdate.message)
                .setPositiveButton(Translation.defaultSection.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun showChangelogDialog(appUpdate: AppUpdate) {
        AlertDialog.Builder(this)
                .setTitle(appUpdate.title)
                .setMessage(appUpdate.message)
                .setNegativeButton(appUpdate.negativeBtn) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun showForceDialog(appUpdate: AppUpdate) {
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

    private fun startPlayStore() {
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

    override fun showPosts(posts: List<Post>) {
        for (post in posts) {
            Timber.e(post.toString())
        }
    }

    override fun showError(msg: String) {
        Timber.e(msg)
    }
}
