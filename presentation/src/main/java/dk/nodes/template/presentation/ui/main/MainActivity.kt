package dk.nodes.template.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dk.nodes.template.domain.extensions.guard
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.extensions.observeNonNull
import dk.nodes.template.presentation.ui.base.BaseActivity
import dk.nodes.template.presentation.util.consume

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.observeNonNull(this) { state ->
            handleNStack(state)
        }
        savedInstanceState.guard { viewModel.checkNStack() }
    }

    private fun handleNStack(viewState: MainActivityViewState) {
        viewState.nstackMessage.consume { showMessageDialog(it) }
        viewState.nstackRateReminder.consume { showRateReminderDialog(it) }
        viewState.nstackUpdate.consume { showChangelogDialog(it) }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
    }
}