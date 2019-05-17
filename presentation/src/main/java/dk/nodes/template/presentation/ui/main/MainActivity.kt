package dk.nodes.template.presentation.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.databinding.ActivityMainBinding
import dk.nodes.template.presentation.extensions.observe
import dk.nodes.template.presentation.extensions.observeNonNull
import dk.nodes.template.presentation.nstack.Translation
import dk.nodes.template.presentation.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.hockeyapp.android.UpdateManager

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        viewModel = bindViewModel()
        viewModel.viewState.observe(this, binding::setViewState)
        viewModel.viewState.observeNonNull(this, ::showErrorMessage)
        // setupNstack()
        // setupHockey()
        viewModel.fetchPosts()
    }

    override fun onDestroy() {
        super.onDestroy()
        // If we checked for hockey updates, unregister
        UpdateManager.unregister()
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
