package dk.eboks.app.presentation.ui.message.components.opening.protectedmessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
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
        loginTv.visibility = View.VISIBLE
        setupTopBar()
        updateTranslation()
        iconIv.setImageDrawable(resources.getDrawable(R.drawable.icon_48_lock_white))
    }

    override fun onResume() {
        super.onResume()
        NStack.addLanguageChangeListener(onLanguageChange)
    }

    override fun onPause() {
        NStack.removeLanguageChangeListener(onLanguageChange)
        super.onPause()
    }

    private fun updateTranslation()
    {
        // mocking login providers
        var nemidProvider = LoginProvider("1","NemId",true,0,null,null,"fallback string")
        var mobileProvider = LoginProvider("2","Mobile Access",true,0,null,null,"fallback string")

        loginSecureBtn.text = Translation.logoncredentials.logonWithProvider.replace("[provider]",nemidProvider.name)
        loginTv.text = Translation.logoncredentials.logonWithProvider.replace("[provider]",mobileProvider.name)
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
}