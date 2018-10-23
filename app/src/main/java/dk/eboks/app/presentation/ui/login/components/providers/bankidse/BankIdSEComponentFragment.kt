package dk.eboks.app.presentation.ui.login.components.providers.bankidse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.WebView
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseWebFragment
import dk.eboks.app.presentation.base.ViewErrorController
import dk.eboks.app.presentation.ui.login.components.providers.WebLoginContract
import dk.eboks.app.presentation.ui.start.screens.StartActivity
import kotlinx.android.synthetic.main.fragment_base_web.*
import timber.log.Timber
import javax.inject.Inject
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * Created by bison on 09-02-2018.
 */
class BankIdSEComponentFragment : BaseWebFragment(), WebLoginContract.View {

    @Inject
    lateinit var presenter : BankIdSEComponentPresenter

    var loginUser: User? = null

    override val defaultErrorHandler: ViewErrorController by lazy {
        ViewErrorController(context = context, closeFunction = {activity.finish()} )
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupTopBar()

        presenter.setup()
        mainTb.title = Translation.loginproviders.bankSeTitle
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_close_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            if(!closeLoginOnBack) {
                presenter.cancelAndClose()
            }
            else {
                activity.finish()
            }
        }
    }

    fun openBankId(url : String) : Boolean
    {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val packageManager = activity.packageManager
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i)
            return true
        } else {
            Timber.d("No Intent available to handle action")
            return false
        }
    }

    override fun onOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let {
            if(it.startsWith("bankid://"))
            {
                Timber.e("Detected BankId deeplink")
                val result = openBankId(url)
                if(!result) // if mobile pay not installed on device
                {
                    AlertDialog.Builder(context)
                            .setTitle(Translation.bankidsupport.bankIdNotInstalledTitle)
                            .setMessage(Translation.bankidsupport.bankidNotInstalledMessage)
                            .setPositiveButton(Translation.bankidsupport.installBankIdBtn.toUpperCase()) { dialog, which ->
                                val appPackageName = "com.bankid.bus"
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
                }
                return true
            }
        }
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
        if(activity is StartActivity)
            (activity as StartActivity).startMain()
        else
            finishActivity(Activity.RESULT_OK)
    }

    override fun showError(viewError: ViewError) {
        showErrorDialog(viewError)
    }

    private fun showDebugDialog()
    {
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

    override fun close() {
        fragmentManager.popBackStack()
    }

    override fun loginKspToken(kspwebtoken: String) {
        presenter.login(kspwebtoken)
    }

    override fun onCheckMergeAccountStatus() {
        presenter.mergeAccountOrKeepSeparated()
    }
}