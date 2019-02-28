package dk.eboks.app.presentation.ui.profile.components.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentFragment
import kotlinx.android.synthetic.main.fragment_profile_verify_email_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EmailVerificationComponentFragment : BaseFragment(), dk.eboks.app.profile.EmailVerificationComponentContract.View {

    @Inject
    lateinit var presenter: dk.eboks.app.profile.EmailVerificationComponentContract.Presenter

    var mail: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_verify_email_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        mail = arguments?.getString("mail")

        mail?.let {
            bodyTv.text = Translation.profile.verifyEmailText.replace("[email]", it, false)
        }

        verifyBtn.setOnClickListener {
            mail?.let {
                presenter.verifyMail(it)
            }
        }

        cancelTv.setOnClickListener {
            getBaseActivity()?.onBackPressed()
        }

        mail?.let {
            verifyBtn.isEnabled = false
            presenter.verifyMail(it)
        }
        ProfileInfoComponentFragment.refreshOnResume = true
    }

    override fun setVerifyBtnEnabled(enabled: Boolean) {
        verifyBtn.isEnabled = enabled
    }
}