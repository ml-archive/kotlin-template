package dk.nodes.template.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dk.nodes.template.R
import dk.nodes.template.domain.models.Translation
import dk.nodes.template.presentation.base.BaseActivity
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
        viewModel.viewState.observe(this, Observer(this::showLoading))
        viewModel.viewState.observe(this, Observer(this::showPosts))
        viewModel.viewState.observe(this, Observer(this::showErrorMessage))
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
        postsProgressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(state: MainActivityViewState) {
        state.errorMessage?.let {
            if (it.consumed) return@let

            Snackbar.make(postsTextView, it.consume() ?: Translation.error.unknownError, Snackbar.LENGTH_SHORT)
        }
    }
}
