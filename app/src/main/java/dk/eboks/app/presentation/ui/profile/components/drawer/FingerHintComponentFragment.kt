package dk.eboks.app.presentation.ui.profile.components.drawer

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfo
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.dialogs.CustomFingerprintDialog
import dk.nodes.locksmith.core.models.FingerprintDialogEvent
import kotlinx.android.synthetic.main.fragment_profile_enable_fingerprint_mobile_component.*
import timber.log.Timber
import javax.inject.Inject

class FingerHintComponentFragment : BaseFragment(), FingerHintComponentContract.View {
    @Inject
    lateinit var presenter: FingerHintComponentContract.Presenter

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(
                R.layout.fragment_profile_enable_fingerprint_mobile_component,
                container,
                false
        )
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        okBtn.setOnClickListener {
            activity.onBackPressed()
        }
    }

    override fun getUserLoginInfo(): LoginInfo {
        return arguments.getParcelable(LoginInfo.KEY)
    }

    private fun onSignUpBtnClicked() {
        Timber.d("onSignUpBtnClicked")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showFingerprintDialog()
        }
    }

    override fun finishView() {
        getBaseActivity()?.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showFingerprintDialog() {
        val customFingerprintDialog = CustomFingerprintDialog(context)

        customFingerprintDialog.setOnFingerprintDialogEventListener {
            customFingerprintDialog.dismiss()

            when (it) {
                FingerprintDialogEvent.CANCEL  -> {
                    // Do nothing?
                }
                FingerprintDialogEvent.SUCCESS -> {
                    presenter.encryptUserLoginInfo()
                }
                FingerprintDialogEvent.ERROR_CIPHER,
                FingerprintDialogEvent.ERROR_ENROLLMENT,
                FingerprintDialogEvent.ERROR_HARDWARE,
                FingerprintDialogEvent.ERROR_SECURE,
                FingerprintDialogEvent.ERROR   -> {
                    showErrorDialog(
                            ViewError(
                                    Translation.error.genericTitle,
                                    Translation.androidfingerprint.errorGeneric,
                                    true,
                                    false
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
}