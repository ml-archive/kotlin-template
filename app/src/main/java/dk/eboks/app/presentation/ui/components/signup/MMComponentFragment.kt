package dk.eboks.app.presentation.ui.components.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import kotlinx.android.synthetic.main.fragment_signup_mm_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MMComponentFragment : BaseFragment(), SignupComponentContract.MMView {

    @Inject
    lateinit var presenter : SignupComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_signup_mm_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        continueWithoutMMTv.setOnClickListener { onContinueClicked() }
        getBaseActivity()?.setToolbar(R.drawable.ic_red_back, Translation.signup.title, null, {
            fragmentManager.popBackStack()
        })
    }

    override fun setupTranslations() {
        headerTv.text = Translation.signup.mmHeader
        detailTv.text = Translation.signup.mmDetail
        signupWithMMBtn.text = Translation.signup.signupWithMMButton
        continueWithoutMMTv.text = Translation.signup.continueWithoutMMButton
        cprTil.hint = Translation.signup.cprHint
    }

    override fun showError() {

    }

    override fun showProgress(show: Boolean) {
        content.visibility = if(show) View.GONE else View.VISIBLE
        progress.visibility = if(show) View.VISIBLE else View.GONE
    }

    fun onContinueClicked()
    {
        (activity as StartActivity).showLogo(false)
        showProgress(true)
        content.postDelayed({
            showProgress(false)
            (activity as StartActivity).replaceFragment(CompletedComponentFragment())
        }, 1000)
    }
}