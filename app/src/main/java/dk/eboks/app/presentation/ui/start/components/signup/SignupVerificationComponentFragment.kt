package dk.eboks.app.presentation.ui.start.components.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import kotlinx.android.synthetic.main.fragment_signup_verification_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class SignupVerificationComponentFragment : BaseFragment(),
    SignupComponentContract.VerificationView {

    @Inject
    lateinit var presenter: SignupComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup_verification_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        VerificationComponentFragment.verificationSucceeded = false
        verifyBtn.setOnClickListener {
            val args = Bundle()
            args.putBoolean("signupVerification", true)
            getBaseActivity()?.openComponentDrawer(VerificationComponentFragment::class.java, args)
        }
        continueWithoutVerificationBtn.setOnClickListener { onContinueClicked() }
        setupTopBar()
        // TODO I should be handled by brian translate
        continueWithoutVerificationBtn.text = Translation.signup.continueWithoutVerificationButton
    }

    override fun onResume() {
        super.onResume()
        if (VerificationComponentFragment.verificationSucceeded) {
            getBaseActivity()?.addFragmentOnTop(
                R.id.containerFl,
                AcceptTermsComponentFragment(),
                true
            )
            VerificationComponentFragment.verificationSucceeded = false
        }
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = Translation.signup.title
        mainTb.setBackgroundColor(
            ContextCompat.getColor(
                context ?: return,
                R.color.backgroundColor
            )
        )
        mainTb.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    override fun showProgress(show: Boolean) {
        content.visibility = if (show) View.GONE else View.VISIBLE
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun onContinueClicked() {
        // (activity as StartActivity).showLogo(false)
        showProgress(true)
        content.postDelayed({
            showProgress(false)
            // remove MM as it is out of scope for the current release
//            if(Config.isSE()) {
//                getBaseActivity()?.addFragmentOnTop(R.id.containerFl, MMComponentFragment(), true)
//            } else {
//                getBaseActivity()?.addFragmentOnTop(R.id.containerFl, AcceptTermsComponentFragment(), true)
//            }
            getBaseActivity()?.addFragmentOnTop(
                R.id.containerFl,
                AcceptTermsComponentFragment(),
                true
            )
        }, 1000)
    }
}