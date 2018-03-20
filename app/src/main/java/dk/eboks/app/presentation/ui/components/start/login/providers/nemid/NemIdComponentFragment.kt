package dk.eboks.app.presentation.ui.components.start.login.providers.nemid

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.WebView
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseWebFragment
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import kotlinx.android.synthetic.main.fragment_base_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class NemIdComponentFragment : BaseWebFragment(), NemIdComponentContract.View {

    @Inject
    lateinit var presenter : NemIdComponentContract.Presenter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        webView.loadData("Nem id webview placeholder", "text/html", "utf8")
        setupTopBar()
        if(BuildConfig.DEBUG) {
            Handler(Looper.getMainLooper()).postDelayed({
                showDebugDialog()
            }, 500)
        }
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

    override fun login(user: User) {

    }

    override fun proceed() {
        (activity as StartActivity).startMain()
    }

    private fun showDebugDialog()
    {
        AlertDialog.Builder(activity)
                .setTitle("Debug")
                .setMessage("Press okay to simulate a successful login with external login provider")
                .setPositiveButton("Login") { dialog, which ->

                    (activity as StartActivity).startMain()
                }
                .setNegativeButton("Close") { dialog, which ->

                }
                .show()
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.red_navigationbar)
        mainTb.setNavigationOnClickListener {
            presenter.cancelAndClose()
        }
    }

    override fun setupTranslations() {
        mainTb.title = "_NemID"
    }

    override fun onOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }

    override fun onLoadFinished(view: WebView?, url: String?) {

    }

    override fun close() {
        fragmentManager.popBackStack()
    }
}