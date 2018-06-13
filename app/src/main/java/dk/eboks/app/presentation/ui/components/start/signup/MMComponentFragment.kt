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
import dk.eboks.app.util.isValidCpr
import kotlinx.android.synthetic.main.fragment_signup_mm_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
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
        //todo what should happend if you continue without ssn ?
        continueWithoutMMTv.setOnClickListener { onContinueClicked() }
        signupWithMMBtn.setOnClickListener { onContinueClicked() }
        setupTopBar()
    }

    private fun setupValidation() {
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

    override fun onResume() {
        super.onResume()
        setupValidation()
    }

    override fun onPause() {
        mHandler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = Translation.signup.title
        mainTb.setNavigationOnClickListener {
            activity.onBackPressed()
        }
    }

    override fun showProgress(show: Boolean) {
        content.visibility = if (show) View.GONE else View.VISIBLE
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun onContinueClicked() {
        //(activity as StartActivity).showLogo(false)
        showProgress(true)
        presenter.verifySSN(cprEt.text.toString())
    }

    override fun ssnExists(ssnExisits: Boolean) {
        showProgress(false)
        if (!ssnExisits) {
            content.postDelayed({
                showProgress(false)
                getBaseActivity()?.addFragmentOnTop(R.id.containerFl, AcceptTermsComponentFragment(), true)
            }, 1000)
        } else {
            // todo ssn already exisits show dialog
        }
    }
}