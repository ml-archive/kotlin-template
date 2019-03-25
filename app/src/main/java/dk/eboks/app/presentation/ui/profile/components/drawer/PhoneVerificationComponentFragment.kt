package dk.eboks.app.presentation.ui.profile.components.drawer

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
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentFragment
import dk.eboks.app.profile.presentation.ui.components.drawer.PhoneVerificationComponentContract
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_profile_verify_mobile_number_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class PhoneVerificationComponentFragment : BaseFragment(), PhoneVerificationComponentContract.View {

    @Inject
    lateinit var presenter: PhoneVerificationComponentContract.Presenter

    var handler = Handler()
    private var codeIsValid = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_profile_verify_mobile_number_component,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setup()
        arguments?.getString("mobile")?.let { presenter.setup(it) }
        ProfileInfoComponentFragment.refreshOnResume = true
    }

    private fun setup() {
        setupValidationListener()
        setEnableStateContinueButton()
        verifyBtn.setOnClickListener {
            val code = verificationCodeEt.text.toString()
            if (!code.isEmpty())
                presenter.confirmMobile(code)
        }

        resendBtn.setOnClickListener {
            presenter.resendVerificationCode()
        }

        cancelBtn.setOnClickListener {
            finishActivity()
        }
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onResume() {
        setupValidationListener()
        super.onResume()
    }

    private fun setupValidationListener() {
        verificationCodeEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(password: Editable?) {
                setEnableStateContinueButton()
                handler.postDelayed({
                    setErrorMessages()
                }, 1200)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setEnableStateContinueButton() {
        codeIsValid = !verificationCodeEt.text.isNullOrBlank()
        verifyBtn.isEnabled = codeIsValid
    }

    private fun setErrorMessages() {
        if (codeIsValid) {
            verificationCodeTil.error = null
        } else {
            verificationCodeTil.error = Translation.logoncredentials.invalidVerification
        }
    }

    override fun showProgress(show: Boolean) {
        progressFl.isVisible = (show)
    }

    override fun showNumber(mobile: String) {
        verifyNumberSubTv.text =
            Translation.profile.verifyMobilText.replace("[mobileNumber]", mobile)
    }
}