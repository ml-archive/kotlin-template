package dk.eboks.app.presentation.ui.message.components.opening.protectedmessage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.mail.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentContract
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.login.screens.PopupLoginActivity
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningActivity
import dk.eboks.app.util.ViewControl
import dk.eboks.app.util.guard
import dk.eboks.app.util.translatedName
import dk.eboks.app.util.visible
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_mail_opening_error_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ProtectedMessageComponentFragment : BaseFragment(), ProtectedMessageComponentContract.View {

    @Inject lateinit var presenter: ProtectedMessageComponentContract.Presenter

    private var protectedMessage: Message? = null

    private val onLanguageChange: (Locale) -> Unit = {
        Timber.e("Locale changed to locale")
        updateTranslation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mail_opening_error_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        loginSecureBtn.visibility = View.VISIBLE
        loginTv.visibility = View.GONE
        // progressPb.visibility = View.GONE
        setupTopBar()
        updateTranslation()
        iconIv.setImageResource(R.drawable.icon_48_lock_white)
        arguments?.getString("loginProviderId")?.let { loginProviderId ->
            setLoginProvider(loginProviderId)
        }

        arguments?.getParcelable<Message>(Message::class.java.simpleName)?.let {
            protectedMessage = it
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
    private fun setLoginProvider(loginProviderId: String) {
        Timber.e("Configuring for login provider $loginProviderId")

        Config.getLoginProvider(loginProviderId)?.let { provider ->
            mainTv.text = Translation.message.protectedMessage.replace(
                "[logonProvider]",
                provider.translatedName()
            )
            loginSecureBtn.text = Translation.logoncredentials.logonWithProvider.replace(
                "[provider]",
                provider.translatedName()
            )
            loginSecureBtn.setOnClickListener {
                val intent = Intent(
                    context,
                    PopupLoginActivity::class.java
                ).putExtra("selectedLoginProviderId", loginProviderId).putExtra("reauth", true)
                startActivityForResult(intent, PopupLoginActivity.REQUEST_VERIFICATION)
            }
        }.guard {
            // hide relog button for now
            loginSecureBtn.visible = (false)
        }
        // loginTv.text = Translation.logoncredentials.logonWithProvider.replace("[provider]",mobileProvider.name)
    }

    private fun updateTranslation() {
        mainTb.title = Translation.message.protectedTitle
        headerTv.text = Translation.message.protectedTitle
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            presenter.setShouldProceed(false)
            activity?.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PopupLoginActivity.REQUEST_VERIFICATION) {
            if (resultCode == Activity.RESULT_OK) {
                Timber.e("Got result ok from login provider, reload message")
                ViewControl.refreshAllOnResume()
                protectedMessage?.let {
                    if (it.id != "0")
                        (activity as MessageOpeningActivity).openMessage(it)
                    else
                        finishActivity()
                }.guard { finishActivity() }
            } else {
                Timber.e("Got result cancel from login provider, doing nothing")
                finishActivity()
            }
        }
    }
}