package dk.eboks.app.presentation.ui.start.components.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.keychain.presentation.components.SignupComponentContract
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.start.screens.StartActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_signup_completed_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class CompletedComponentFragment : BaseFragment(), SignupComponentContract.CompletedView {

    @Inject
    lateinit var presenter: SignupComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup_completed_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        continueBtn.setOnClickListener { onContinueClicked() }
    }

    override fun showProgress(show: Boolean) {
        content.isVisible = !show
        progress.isVisible = show
    }

    private fun onContinueClicked() {
        (activity as StartActivity).startMain()
    }
}