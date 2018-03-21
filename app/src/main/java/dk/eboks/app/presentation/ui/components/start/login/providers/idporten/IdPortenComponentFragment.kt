package dk.eboks.app.presentation.ui.components.start.login.providers.idporten

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseWebFragment
import kotlinx.android.synthetic.main.fragment_base_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class IdPortenComponentFragment : BaseWebFragment(), IdPortenComponentContract.View {

    @Inject
    lateinit var presenter : IdPortenComponentContract.Presenter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        webView.loadData("Nem id webview placeholder", "text/html", "utf8")

        setupTopBar()
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.red_navigationbar)
        mainTb.setNavigationOnClickListener {
            activity.onBackPressed()
        }
    }

    override fun setupTranslations() {
        mainTb.title = Translation.loginproviders.idPortenTitle
    }

    override fun onOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }

    override fun onLoadFinished(view: WebView?, url: String?) {

    }
}