package dk.nodes.template.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import dk.nodes.nstack.kotlin.NStack
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

    private fun setupHockey() {
        // Auto-send crashes without asking user
        if(BuildConfig.DEBUG) {
            CrashManager.register(this, object : CrashManagerListener() {
                override fun shouldAutoUploadCrashes(): Boolean {
                    return true
                }
            })
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
                    // Do nothing because there is no update
                }
                AppUpdateState.UPDATE -> {
                    // Show a user a dialog that is dismissible
                }
                AppUpdateState.FORCE -> {
                    // Show the user an undismissable dialog
                }
                AppUpdateState.CHANGELOG -> {
                    // Show change log (Not yet implemented because its never used)
                }
            }
        }
        NStack.appOpen({ success -> Log.e("debug", "appopen success = $success") })
    }


    override fun setupTranslations() {
        textview.text = Translation.defaultSection.settings
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
