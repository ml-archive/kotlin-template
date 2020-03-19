package dk.nodes.template.presentation.ui.sample

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.extensions.observeNonNull
import dk.nodes.template.presentation.ui.base.BaseFragment
import dk.nodes.template.presentation.util.consume
import kotlinx.android.synthetic.main.fragment_sample.*

class SampleFragment : BaseFragment(R.layout.fragment_sample) {

    override val viewModel by viewModel<SampleViewModel>()
    private lateinit var adapter: SampleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchPosts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragSampleFab.setOnClickListener { viewModel.switchTheme() }
        adapter = SampleAdapter().also(rv::setAdapter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.viewState.observeNonNull(this) { state ->
            showLoading(state)
            showPosts(state)
            showErrorMessage(state)
        }
    }

    private fun showPosts(state: SampleViewState) {
        adapter.setData(state.posts)
    }

    private fun showLoading(state: SampleViewState) {
        postsProgressBar.isVisible = state.isLoading
        rv.isVisible = !state.isLoading
    }

    private fun showErrorMessage(state: SampleViewState) {
        state.viewError.consume { viewError ->
            defaultErrorController.get().showErrorSnackbar(requireView(), viewError) {
                viewModel.fetchPosts()
            }
        }
    }
}