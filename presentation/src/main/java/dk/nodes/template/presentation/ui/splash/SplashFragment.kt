package dk.nodes.template.presentation.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.observe
import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.AppUpdateState
import dk.nodes.nstack.kotlin.models.state
import dk.nodes.nstack.kotlin.models.update
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.ui.base.BaseFragment
import dk.nodes.template.presentation.ui.main.MainActivity
import dk.nodes.template.presentation.util.consume

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel by viewModel<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            handleNStack(state)
            handleNavigation(state)
        }
        viewModel.initAppState()
    }

    private fun handleNStack(state: SplashViewState) {
        state.nstackUpdateAvailable.consume { appUpdate ->
            when (appUpdate.state) {
                AppUpdateState.FORCE -> {
                    showForceDialog(appUpdate)
                }
                // We handle the rest in MainActivity
                else -> {
                }
            }
        }
    }

    private fun handleNavigation(state: SplashViewState) {
        if (state.doneLoading && state.nstackUpdateAvailable?.peek()?.state != AppUpdateState.FORCE) {
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