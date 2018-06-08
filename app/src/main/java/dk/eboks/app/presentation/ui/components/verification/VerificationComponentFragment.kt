package dk.eboks.app.presentation.ui.components.verification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.SheetComponentActivity
import dk.eboks.app.presentation.ui.screens.login.PopupLoginActivity
import kotlinx.android.synthetic.main.fragment_verification_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class VerificationComponentFragment : BaseFragment(), VerificationComponentContract.View {

    @Inject
    lateinit var presenter : VerificationComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_verification_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        cancelTv.setOnClickListener {
            (activity as SheetComponentActivity).onBackPressed()
        }

        when (Config.getCurrentConfigName()){
            "danish" ->{
                headerTv.text = Translation.signup.signOnNemIdTitle
                detailTv.text = Translation.signup.signOnNemIdMessage
                verifyBtn.text = Translation.signup.signOnNemIdButton
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

        verifyBtn.setOnClickListener {
            // start popuploginactivity for result
            startActivityForResult(Intent(context, PopupLoginActivity::class.java), 100)
        }

    }
}