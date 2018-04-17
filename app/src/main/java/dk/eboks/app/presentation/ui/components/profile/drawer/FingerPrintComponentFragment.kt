package dk.eboks.app.presentation.ui.components.profile.drawer

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.profile.drawer.FingerPrintComponentContract.Mode.EMAIL
import dk.eboks.app.presentation.ui.components.profile.drawer.FingerPrintComponentContract.Mode.SOCIAL_SECURITY
import dk.eboks.app.util.addAfterTextChangeListener
import dk.eboks.app.util.isValidCpr
import dk.eboks.app.util.setVisible
import kotlinx.android.synthetic.main.fragment_profile_enable_fingerprint_component.*
import javax.inject.Inject

class FingerPrintComponentFragment : BaseFragment(), FingerPrintComponentContract.View {
    @Inject
    lateinit var presenter: FingerPrintComponentContract.Presenter

    var handler = Handler()
    var mode: FingerPrintComponentContract.Mode? = null

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(
                R.layout.fragment_profile_enable_fingerprint_component,
                container,
                false
        )
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    // Setters

    override fun setProviderMode(mode: FingerPrintComponentContract.Mode) {
        this.mode = mode

        when (mode) {
            EMAIL           -> {
                setupEmailFingerprintEnrollment()
            }
            SOCIAL_SECURITY -> {
                setupSocialSecurityEnrollment()
            }
        }
    }

    // Setup Methods

    private fun setupEmailFingerprintEnrollment() {
        socialSecurityTil.setVisible(false)
        
        setupPasswordListener()
    }

    private fun setupSocialSecurityEnrollment() {
        socialSecurityTil.setVisible(true)

        setupSocialSecurityListeners()
        setupPasswordListener()
    }

    private fun setupPasswordListener() {
        passwordEt.addAfterTextChangeListener {
            when (mode) {
                EMAIL           -> {
                    checkEmailContinueState()
                }
                SOCIAL_SECURITY -> {
                    checkCprContinueState()
                }
            }

            handler.postDelayed({ checkPasswordValidity() }, 1200)
        }
    }

    private fun setupSocialSecurityListeners() {
        socialSecurityEt.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus -> }

        socialSecurityEt.addAfterTextChangeListener {
            socialSecurityTil.error = null
            checkCprContinueState()
            handler.postDelayed({ checkSocialSecurityValidity() }, 1200)
        }
    }

    // State Checkers

    private fun checkEmailContinueState() {
        val passwordIsValid = (!passwordEt.text.isNullOrBlank())
        enableBtn.isEnabled = passwordIsValid
    }

    private fun checkCprContinueState() {
        val cprIsValid = (socialSecurityEt.text.isValidCpr())
        val passwordIsValid = (!passwordEt.text.isNullOrBlank())

        val enabled = (cprIsValid && passwordIsValid)
        enableBtn.isEnabled = enabled
    }

    // Life Cycle Functions

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onResume() {
        this.mode?.let { setProviderMode(it) }
        super.onResume()
    }

    // Routing Functions

    private fun startHintFragment() {
        getBaseActivity()?.openComponentDrawer(FingerHintComponentFragment::class.java)
    }

    // Error Handlers

    private fun checkSocialSecurityValidity() {
        val cprIsValid = (socialSecurityEt.text.isValidCpr())

        if (!cprIsValid) {
            socialSecurityTil.error = Translation.logoncredentials.invalidSSN
        } else {
            socialSecurityTil.error = null
        }
    }

    private fun checkPasswordValidity() {
        val passwordIsValid = (!passwordEt.text.isNullOrBlank())

        if (!passwordIsValid) {
            passwordTil.error = Translation.logoncredentials.invalidPassword
        } else {
            passwordTil.error = null
        }
    }
}