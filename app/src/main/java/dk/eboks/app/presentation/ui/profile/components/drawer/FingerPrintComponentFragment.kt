package dk.eboks.app.presentation.ui.profile.components.drawer

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfo
import dk.eboks.app.domain.models.login.LoginInfoType
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.dialogs.CustomFingerprintDialog
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentFragment
import dk.eboks.app.profile.presentation.ui.components.drawer.FingerPrintComponentContract
import dk.eboks.app.util.addAfterTextChangeListener
import androidx.core.view.isInvisible
import dk.eboks.app.util.isValidCpr
import androidx.core.view.isVisible
import dk.nodes.locksmith.core.models.FingerprintDialogEvent
import kotlinx.android.synthetic.main.fragment_profile_enable_fingerprint_component.*
import timber.log.Timber
import javax.inject.Inject

class FingerPrintComponentFragment : BaseFragment(), FingerPrintComponentContract.View {
    @Inject
    lateinit var presenter: FingerPrintComponentContract.Presenter

    var handler = Handler()
    private var mode: LoginInfoType? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_profile_enable_fingerprint_component,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        enableBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                verifyLoginCredentials()
                // showFingerprintDialog()
            }
        }

        redOptionTv.setOnClickListener {
            activity?.onBackPressed()
        }
        ProfileInfoComponentFragment.refreshOnResume = true
    }

    // Setters
    override fun setProviderMode(mode: LoginInfoType) {
        this.mode = mode

        when (mode) {
            LoginInfoType.EMAIL -> {
                setupEmailFingerprintEnrollment()
            }
            LoginInfoType.SOCIAL_SECURITY -> {
                setupSocialSecurityEnrollment()
            }
        }
    }

    // Setup Methods
    private fun verifyLoginCredentials() {
        presenter.verifyLoginCredentials()
    }

    private fun setupEmailFingerprintEnrollment() {
        Timber.d("setupEmailFingerprintEnrollment")

        socialSecurityTil.isVisible = false

        setupPasswordListener()
    }

    private fun setupSocialSecurityEnrollment() {
        Timber.d("setupSocialSecurityEnrollment")

        socialSecurityTil.isVisible = true

        setupSocialSecurityListeners()
        setupPasswordListener()
    }

    private fun setupPasswordListener() {
        passwordEt.addAfterTextChangeListener {
            when (mode) {
                LoginInfoType.EMAIL -> {
                    checkEmailContinueState()
                }
                LoginInfoType.SOCIAL_SECURITY -> {
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
    /*
    private fun startHintFragment() {
     mode?.let {
             val bundle = Bundle()
             val loginInfo = LoginInfo(it,socialSecurityEt.text.toString(),passwordEt.text.toString())
             bundle.putParcelable(LoginInfo.KEY,loginInfo)
             getBaseActivity()?.onBackPressed()
             getBaseActivity()?.openComponentDrawer(FingerHintComponentFragment::class.java, bundle)
        }
    }
    */

    override fun getUserLoginInfo(): LoginInfo {
        mode?.let {
            return LoginInfo(it, socialSecurityEt.text.toString(), passwordEt.text.toString())
        }
        throw(IllegalStateException("mode must not be null"))
    }

    override fun proceedAfterLoginVerification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showFingerprintDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showFingerprintDialog() {
        val customFingerprintDialog = CustomFingerprintDialog(context ?: return)

        customFingerprintDialog.setOnFingerprintDialogEventListener {
            customFingerprintDialog.dismiss()

            when (it) {
                FingerprintDialogEvent.CANCEL -> {
                    showProgress(false)
                }
                FingerprintDialogEvent.SUCCESS -> {

                    presenter.encryptUserLoginInfo()
                }
                FingerprintDialogEvent.ERROR_CIPHER,
                FingerprintDialogEvent.ERROR_ENROLLMENT,
                FingerprintDialogEvent.ERROR_HARDWARE,
                FingerprintDialogEvent.ERROR_SECURE,
                FingerprintDialogEvent.ERROR -> {
                    showProgress(false)
                    showErrorDialog(
                        ViewError(
                            Translation.error.genericTitle,
                            Translation.androidfingerprint.errorGeneric,
                            shouldDisplay = true,
                            shouldCloseView = false
                        )
                    )
                }
            }
        }

        customFingerprintDialog.onUsePasswordBtnListener = {
            // Todo add use password section
        }

        customFingerprintDialog.show()
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

    override fun finishView() {
        getBaseActivity()?.onBackPressed()
    }

    override fun showProgress(show: Boolean) {
        buttonsLl.isInvisible = show
        progressPb.isVisible = show
    }
}