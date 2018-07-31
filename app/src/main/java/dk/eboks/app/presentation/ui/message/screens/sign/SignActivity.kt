package dk.eboks.app.presentation.ui.message.screens.sign

import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import dk.eboks.app.R
import dk.eboks.app.domain.interactors.message.GetSignLinkInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCardLinkInteractorImpl
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_base_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class SignActivity : BaseActivity(), SignContract.View {
    @Inject
    lateinit var presenter: SignContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_base_web)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
        setupWebView()
        intent.getSerializableExtra(Message::class.java.simpleName)?.let { msg ->
            msg as Message
            presenter.setup(msg)
        }
    }

    private fun setupTopBar()
    {
        mainTb.setNavigationIcon(R.drawable.icon_48_close_red_navigationbar)
        mainTb.title = Translation.sign.title
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun setupWebView()
    {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.useWideViewPort = false
        settings.allowFileAccess = true

        // Performance improvements
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        CookieManager.getInstance().setAcceptCookie(true) // Set this after WebView init but before load
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        }

        // Enable local storage
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.saveFormData = false

        // Disable pinch zoom and zoom controls
        settings.builtInZoomControls = false
        settings.displayZoomControls = false

        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView.overScrollMode = WebView.OVER_SCROLL_ALWAYS
        webView.isScrollbarFadingEnabled = false
        webView.isVerticalScrollBarEnabled = true

        // Avoid long click selection of UI elements
        webView.setOnLongClickListener { true }

        //webView.setWebViewClient(new WebViewClient());

        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        //webView.loadUrl(loginUrl)

        webView.getSettings().setJavaScriptEnabled(true)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if(!onOverrideUrlLoading(view, url))
                    return super.shouldOverrideUrlLoading(view, url)
                else
                    return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                onLoadFinished(view, url)
            }
        }
    }

    fun onOverrideUrlLoading(view: WebView?, url: String?) : Boolean
    {
        Timber.e("URL override: $url")
        url?.let {
            if(url.contains(GetSignLinkInteractorImpl.SUCCESS_CALLBACK))
            {
                finishAfterTransition()
            }
            if(url.contains(GetSignLinkInteractorImpl.CANCEL_CALLBACK))
            {
                finishAfterTransition()
            }
            if(url.contains(GetSignLinkInteractorImpl.ERROR_CALLBACK))
            {
                val ve = ViewError()
                ve.shouldCloseView = true
                showErrorDialog(ve)
            }
        }
        return false
    }

    fun onLoadFinished(view: WebView?, url: String?)
    {

    }

    override fun loadUrl(urlString: String) {
        webView.loadUrl(urlString)
    }

    override fun loadData(data: String) {

    }
}