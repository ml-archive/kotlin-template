package dk.eboks.app.presentation.ui.login.components.providers.nemid

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseWebFragment
import dk.eboks.app.presentation.ui.login.components.providers.WebLoginContract
import dk.eboks.app.presentation.ui.start.screens.StartActivity
import kotlinx.android.synthetic.main.fragment_base_web.*
import timber.log.Timber
import javax.inject.Inject
import kotlinx.android.synthetic.main.include_toolbar.*
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog


/**
 * Created by bison on 09-02-2018.
 */
class NemIdComponentFragment : BaseWebFragment(), WebLoginContract.View {

    @Inject
    lateinit var presenter : NemIdComponentPresenter

    var loginUser: User? = null
    var didAttemptToInstallNemID = false

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
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Timber.e("Injecting js")
                // TODO this is not compatible with older versions of android, use loadurl with javascript: instead
                //view.loadUrl("javascript:" + getJS())
                view.evaluateJavascript(getJS(), null)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        getBaseActivity()?.backPressedCallback = {
            Timber.e("closeLoginOnBack in nemidfragment is $closeLoginOnBack")
            if(!closeLoginOnBack) {
                presenter.cancelAndClose()
            }
            else
            {
                activity.finish()
            }
            true
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

        //Timber.e(getJS())

        webView.loadUrl(loginUrl)
    }

    override fun proceed() {
        if(activity is StartActivity)
            (activity as StartActivity).startMain()
        else
            finishActivity(Activity.RESULT_OK)
    }

    override fun showError(viewError: ViewError) {
        showErrorDialog(viewError)
    }
    
    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            if(!closeLoginOnBack) {
                presenter.cancelAndClose()
            }
            else
            {
                activity.finish()
            }
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

    private fun openNemIdApp()
    {
        //app switch test
        val secondFactorIntent = if(Config.getCurrentEnvironmentName()?.contentEquals("production") == false)
            activity?.getPackageManager()?.getLaunchIntentForPackage("dk.e_nettet.mobilekey.everyone.kopi")
        else
            activity?.getPackageManager()?.getLaunchIntentForPackage("dk.e_nettet.mobilekey.everyone")

        if(secondFactorIntent == null)
        {
            AlertDialog.Builder(context)
                    .setTitle(Translation.nemidsupport.nemIdNotInstalledTitle)
                    .setMessage(Translation.nemidsupport.nemIdNotInstalledMessage)
                    .setPositiveButton(Translation.nemidsupport.installNemIdAppBtn.toUpperCase()) { dialog, which ->
                        val appPackageName = "dk.e_nettet.mobilekey.everyone"
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                        } catch (anfe: android.content.ActivityNotFoundException) {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                        }

                    }
                    .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->

                    }
                    .create()
                    .show()
            return
        }

        secondFactorIntent?.setFlags(0)
        startActivityForResult(secondFactorIntent, 0)
        didAttemptToInstallNemID = true
    }

    private inner class WebAppInterfaceNemID {
        @JavascriptInterface
        fun performAppSwitch() {
            openNemIdApp()
        }
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println(requestCode)
        super.onActivityResult(requestCode, resultCode, data)
    }
    */


    fun getJS(): String {
        return "function onNemIDMessage(e) \n" +
                "{ \n" +
                "    var event = e || event;\n" +
                "    var message = JSON.parse(event.data);\n" +
                "    if(message.command === \"AwaitingAppApproval\") \n" +
                "    {\n" +
                "        NemIDActivityJSI.performAppSwitch();\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "if (window.addEventListener) \n" +
                "{\n" +
                "    window.addEventListener(\"message\", onNemIDMessage);\n" +
                "}\n" +
                "else if (window.attachEvent) \n" +
                "{\n" +
                "    window.attachEvent(\"onmessage\", onNemIDMessage); \n" +
                "}" +
                "console.log(\"registered eventhandlers and shit\");"
    }

    override fun onCheckMergeAccountStatus() {
        presenter.mergeAccountOrKeepSeparated()
    }
}