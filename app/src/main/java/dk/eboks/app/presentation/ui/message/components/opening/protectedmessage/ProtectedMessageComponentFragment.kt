package dk.eboks.app.presentation.ui.message.components.opening.protectedmessage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.login.screens.PopupLoginActivity
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentFragment
import dk.eboks.app.util.guard
import dk.eboks.app.util.setVisible
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_mail_opening_error_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ProtectedMessageComponentFragment : BaseFragment(), ProtectedMessageComponentContract.View {

    @Inject
    lateinit var presenter : ProtectedMessageComponentContract.Presenter

    val onLanguageChange : (Locale)->Unit = { locale ->
        Timber.e("Locale changed to locale")
        updateTranslation()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_mail_opening_error_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        loginSecureBtn.visibility = View.VISIBLE
        loginTv.visibility = View.GONE
        //progressPb.visibility = View.GONE
        setupTopBar()
        updateTranslation()
        iconIv.setImageDrawable(resources.getDrawable(R.drawable.icon_48_lock_white))
        arguments?.getString("loginProviderId")?.let { loginProviderId ->
            setLoginProvider(loginProviderId)
        }
    }

    override fun onResume() {
        super.onResume()
        NStack.addLanguageChangeListener(onLanguageChange)
    }

    override fun onPause() {
        NStack.removeLanguageChangeListener(onLanguageChange)
        super.onPause()
    }

    /*

     */
    private fun setLoginProvider(loginProviderId : String)
    {
        Timber.e("Configuring for login provider $loginProviderId")

        Config.getLoginProvider(loginProviderId)?.let { provider ->
            loginSecureBtn.text = Translation.logoncredentials.logonWithProvider.replace("[provider]", loginProviderId)
            loginSecureBtn.setOnClickListener {
                val intent = Intent(context, PopupLoginActivity::class.java).putExtra("selectedLoginProviderId", loginProviderId).putExtra("reauth", true)
                startActivityForResult(intent, PopupLoginActivity.REQUEST_VERIFICATION)
            }
        }.guard {   // hide relog button for now
            loginSecureBtn.setVisible(false)
        }
        //loginTv.text = Translation.logoncredentials.logonWithProvider.replace("[provider]",mobileProvider.name)
    }

    private fun updateTranslation()
    {
        mainTb.title = Translation.message.protectedTitle
        headerTv.text = Translation.message.protectedTitle
        mainTv.text = Translation.message.protectedMessage
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            presenter.setShouldProceed(false)
            activity.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PopupLoginActivity.REQUEST_VERIFICATION)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                MailListComponentFragment.refreshOnResume = true
                Timber.e("Got result ok from login provider, reload message")
            }
            else
            {
                Timber.e("Got result cancel from login provider, doing nothing")
            }
            finishActivity()
        }
    }
}