package dk.eboks.app.presentation.ui.components.start.signup

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import dk.eboks.app.util.isValidCpr
import kotlinx.android.synthetic.main.fragment_signup_mm_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MMComponentFragment : BaseFragment(), SignupComponentContract.MMView {

    @Inject
    lateinit var presenter: SignupComponentContract.Presenter

    var mHandler = Handler()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_signup_mm_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        continueWithoutMMTv.setOnClickListener { onContinueClicked() }
        getBaseActivity()?.setToolbar(R.drawable.red_navigationbar, Translation.signup.title, null, {
            fragmentManager.popBackStack()
        })

        cprEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(cprNumber: Editable?) {
                mHandler.removeCallbacksAndMessages(null)
                cprTil.error = null
                signupWithMMBtn.isEnabled = cprNumber?.isValidCpr() ?: false
                mHandler?.postDelayed({
                    if (!signupWithMMBtn.isEnabled && !cprEt.text.isNullOrBlank()) {
                        cprTil.error = Translation.signup.mmInvalidCprNumber
                    }
                }, 1200)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
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
        content.visibility = if (show) View.GONE else View.VISIBLE
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun onContinueClicked() {
        //(activity as StartActivity).showLogo(false)
        showProgress(true)
        content.postDelayed({
            showProgress(false)
            (activity as StartActivity).replaceFragment(AcceptTermsComponentFragment())
        }, 1000)
    }

    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}