package dk.eboks.app.presentation.ui.components.start.login.providers.bankidno

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseWebFragment
import dk.eboks.app.presentation.ui.components.start.login.providers.WebLoginContract
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import kotlinx.android.synthetic.main.fragment_base_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class BankIdNOComponentFragment : BaseWebFragment(), WebLoginContract.View {

    @Inject
    lateinit var presenter : BankIdNOComponentPresenter

    var loginUser: User? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()

        presenter.setup()
        mainTb.title = Translation.loginproviders.bankNoTitle
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            activity.onBackPressed()
        }
    }

    override fun onOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }

    override fun onLoadFinished(view: WebView?, url: String?) {

    }

    override fun onResume() {
        super.onResume()
        getBaseActivity()?.backPressedCallback = {
            presenter.cancelAndClose()
            true
        }
    }

    override fun onPause() {
        getBaseActivity()?.backPressedCallback = null
        super.onPause()
    }

    override fun setupLogin(user: User?) {
        loginUser = user
        val loginUrl = "${Config.currentMode.environment?.kspUrl}bankid"
        Timber.e("Opening $loginUrl")
        webView.loadUrl(loginUrl)
    }

    override fun proceed() {
        (activity as StartActivity).startMain()
    }

    override fun showError(viewError: ViewError) {
        showErrorDialog(viewError)
    }


    override fun close() {
        fragmentManager.popBackStack()
    }

    override fun loginKspToken(kspwebtoken: String) {
        presenter.login(kspwebtoken)
    }

}