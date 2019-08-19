package dk.nodes.template.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.extensions.observeNonNull
import dk.nodes.template.presentation.ui.base.BaseActivity
import net.hockeyapp.android.UpdateManager

class MainActivity : BaseActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.viewState.observeNonNull(this) { state ->
            handleNStack(state)
        }
        viewModel.dispatch(MainAction.CheckNstackAction)
    }

    private fun handleNStack(viewState: MainViewState) {
        viewState.nstackMessage?.let { showMessageDialog(it) }
        viewState.nstackRateReminder?.let { showRateReminderDialog(it) }
        viewState.nstackUpdate?.let { showChangelogDialog(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        // If we checked for hockey updates, unregister
        UpdateManager.unregister()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
    }
}
