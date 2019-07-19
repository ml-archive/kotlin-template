package dk.nodes.template.presentation.ui.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.extensions.observeNonNull
import dk.nodes.template.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sample.*

class SampleFragment : BaseFragment() {

    private val viewModel by viewModel<SampleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchPosts()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    private fun showPosts(state: SampleViewState) {
        postsTextView.text = state.posts.joinToString { it.title + System.lineSeparator() }
    }

    private fun showLoading(state: SampleViewState) {
        postsProgressBar.isVisible = state.isLoading
    }

    private fun showErrorMessage(state: SampleViewState) {
        state.errorMessage?.consume()?.let {
            Snackbar.make(postsTextView, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.viewState.observeNonNull(this) { state ->
            showLoading(state)
            showPosts(state)
            showErrorMessage(state)
        }
    }
}