package dk.eboks.app.presentation.ui.components.start.login.providers.nemid

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.*
import dk.eboks.app.BuildConfig
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
class NemIdComponentFragment : BaseWebFragment(), WebLoginContract.View {

    @Inject
    lateinit var presenter : NemIdComponentPresenter

    var loginUser: User? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
        mainTb.title = Translation.loginproviders.nemidTitle
        nemIdSpecificSetup()
        presenter.setup()

    }

    private fun nemIdSpecificSetup()
    {
        webView.addJavascriptInterface(WebAppInterfaceNemID(), "NemIDActivityJSI")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Timber.e("Injecting js")
                // TODO this is not compatible with older versions of android, use loadurl with javascript: instead
                view.evaluateJavascript(getJS(), null)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        getBaseActivity()?.backPressedCallback = {
            if(!closeLoginOnBack) {
                presenter.cancelAndClose()
                true
            }
            else
            {
                activity.finish()
                true
            }
        }

    }

    override fun onPause() {
        getBaseActivity()?.backPressedCallback = null
        super.onPause()
    }

    override fun setupLogin(user: User?) {
        loginUser = user
        val loginUrl = "${Config.currentMode.environment?.kspUrl}nemid"
        Timber.e("Opening $loginUrl")
        webView.loadUrl(loginUrl)
        //webView.loadData("Nem id webview placeholder", "text/html", "utf8")
    }

    override fun proceed() {
        (activity as StartActivity).startMain()
    }

    override fun showError(viewError: ViewError) {
        showErrorDialog(viewError)
    }

    private fun showDebugDialog() {
        AlertDialog.Builder(activity)
                .setTitle("Debug")
                .setMessage("Press okay to simulate a successful login with login provider")
                .setPositiveButton("Login") { dialog, which ->
                    presenter.login("kspToken xx")
                    dialog.dismiss()
                }
                .setNegativeButton("Close") { dialog, which ->
                    webView.postDelayed({ presenter.cancelAndClose() }, 500)
                }
                .show()
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            presenter.cancelAndClose()
        }
    }

    override fun loginKspToken(kspwebtoken: String) {
        presenter.login(kspwebtoken)
    }

    override fun onOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }

    override fun onLoadFinished(view: WebView?, url: String?) {

    }

    override fun close() {
        fragmentManager.popBackStack()
    }

    private inner class WebAppInterfaceNemID {
        @JavascriptInterface
        fun performAppSwitch() {
            Timber.e("Perform ipswitch")
        }

    }

    fun getJS(): String {
        return ("function onNemIDMessage(e) { "
                + "console.log(\"onNemIdMessage\"); "
                + "var event = e || event; "
                + "var win = document.getElementById(\"nemid_iframe\").contentWindow, postMessage = {}, message; "
                + "message = JSON.parse(event.data); "
                + " if (message.command === \"AwaitingAppApproval\") { "
                + "app.performAppSwitch();"
                + "} }")
    }
}