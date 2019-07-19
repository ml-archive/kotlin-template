package dk.nodes.template.presentation.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.AppUpdateState
import dk.nodes.nstack.kotlin.models.Message
import dk.nodes.nstack.kotlin.models.RateReminder
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.extensions.observeNonNull
import dk.nodes.template.presentation.nstack.Translation
import dk.nodes.template.presentation.ui.base.BaseFragment
import dk.nodes.template.presentation.ui.main.*
import dk.nodes.template.presentation.ui.sample.SampleViewModel

class SplashFragment : BaseFragment() {

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.observeNonNull(this) { state ->
            handleNStack(state)
            handleNavigation(state)
        }
        viewModel.initAppState()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun handleNStack(state: SplashViewState) {
        val appUpdate = state.nstackUpdateAvailable?.consume() ?: return
        when (appUpdate.update.state) {
            AppUpdateState.FORCE -> {
                showForceDialog(appUpdate.update)
            }
            // We handle the rest in MainActivity
            else -> {

            }
        }
    }

    private fun handleNavigation(state: SplashViewState) {
        if(state.doneLoading && state.nstackUpdateAvailable?.peek()?.update?.state != AppUpdateState.FORCE) {
            showApp()
        }
    }

    private fun showApp() {
        startActivity(MainActivity.createIntent(requireContext()))
        activity?.overridePendingTransition(0, 0)
    }

    private fun startPlayStore() {
        try {
            startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=${context?.packageName}")
                    )
            )
        } catch (anfe: android.content.ActivityNotFoundException) {
            startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=${context?.packageName}")
                    )
            )
        }
    }

    private fun showForceDialog(appUpdate: AppUpdate) {
        val dialog = AlertDialog.Builder(context ?: return)
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

}