package dk.eboks.app.presentation.ui.login.components.verification

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.SheetComponentActivity
import dk.eboks.app.presentation.ui.login.screens.PopupLoginActivity
import dk.eboks.app.util.translatedName
import kotlinx.android.synthetic.main.fragment_verification_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class VerificationComponentFragment : BaseFragment(), VerificationComponentContract.View {

    @Inject lateinit var presenter: VerificationComponentContract.Presenter
    @Inject lateinit var appConfig: AppConfig

    var signupVerification = false

    companion object {
        var verificationSucceeded = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verification_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        cancelTv.setOnClickListener {
            (activity as SheetComponentActivity).onBackPressed()
        }

        signupVerification = arguments?.getBoolean("signupVerification", false) ?: false

        appConfig.getLoginProvider(appConfig.verificationProviderId ?: "")?.let { provider ->
            headerTv.text = Translation.profile.verifyingAccountTitle.replace(
                "[logonProvider]",
                provider.translatedName()
            )
            detailTv.text = Translation.profile.verifyingAccountBody.replace(
                "[logonProvider]",
                provider.translatedName()
            )
            verifyBtn.text = Translation.profile.logOnWithNemID.replace(
                "[logonProvider]",
                provider.translatedName()
            )
        }

        /*
        when (appConfig.getCurrentConfigName()){
            "danish" ->{
                headerTv.text = Translation.profile.verifyingAccountTitle
                detailTv.text = Translation.profile.verifyingAccountBody
                verifyBtn.text = Translation.profile.logOnWithNemID
            }
            "swedish" ->{
                headerTv.text = Translation.signup.signOnBankIDtitle
                detailTv.text = Translation.signup.signOnBankIDMessage
                verifyBtn.text = Translation.signup.signOnBankIDButton
            }
            "norwegian" -> {
                headerTv.text = Translation.signup.signOnBankIDtitleNOR
                detailTv.text = Translation.signup.signOnBankIDMessageNOR
                verifyBtn.text = Translation.signup.signOnBankIDButtonNOR
            }
            else ->{
                // fallback setting using the danish translations
                headerTv.text = Translation.signup.signOnNemIdTitle
                detailTv.text = Translation.signup.signOnNemIdMessage
                verifyBtn.text = Translation.signup.signOnNemIdButton
            }
        }
        */

        verifyBtn.setOnClickListener {
            // start popuploginactivity for result
            presenter.setupVerificationState(signupVerification)
            val intent = Intent(context, PopupLoginActivity::class.java).putExtra(
                "selectedLoginProviderId",
                appConfig.verificationProviderId
            )
            startActivityForResult(intent, PopupLoginActivity.REQUEST_VERIFICATION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PopupLoginActivity.REQUEST_VERIFICATION) {
            if (resultCode == Activity.RESULT_OK) {
                Timber.e("Got result ok from login provider, closing drawer")
                verificationSucceeded = true
                finishActivity()
            } else {
                verificationSucceeded = false
                Timber.e("Got result cancel from login provider, doing nothing")
            }
        }
    }
}