package dk.nodes.template.presentation.ui.main

import android.os.Bundle
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.ui.base.BaseActivity
import dk.nodes.template.presentation.extensions.observeNonNull
import dk.nodes.template.presentation.nstack.Translation
import kotlinx.android.synthetic.main.activity_main.*
import net.hockeyapp.android.UpdateManager

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // setupNstack()
        // setupHockey()
        viewModel = bindViewModel()
        viewModel.viewState.observeNonNull(this) { state ->
            showLoading(state)
            showPosts(state)
            showErrorMessage(state)
        }
        viewModel.fetchPosts()
    }

    override fun onDestroy() {
        super.onDestroy()
        // If we checked for hockey updates, unregister
        UpdateManager.unregister()
    }

    private fun showPosts(state: MainActivityViewState) {
        postsTextView.text = state.posts.joinToString { it.title + System.lineSeparator() }
    }

    private fun showLoading(state: MainActivityViewState) {
        postsProgressBar.isVisible = state.isLoading
    }

    private fun showErrorMessage(state: MainActivityViewState) {
        state.errorMessage?.let {
            if (it.consumed) return@let

            Snackbar.make(
                postsTextView,
                it.consume() ?: Translation.error.unknownError,
                Snackbar.LENGTH_SHORT
            )
        }
    }
}
