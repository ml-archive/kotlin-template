package dk.eboks.app.presentation.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import dk.eboks.app.R
import dk.eboks.app.presentation.ui.profile.components.drawer.MergeAccountComponentFragment
import kotlinx.android.synthetic.main.fragment_base_web.*
import timber.log.Timber

/**
 * Created by bison on 13/03/2018.
 */
abstract class BaseWebFragment : BaseFragment() {

    var closeLoginOnBack : Boolean = false
    var shouldCheckMergeAccountOnResume = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_base_web, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshSrl.isEnabled = false
        setupWebView()
        Timber.e("Arguments: ${arguments?.containsKey("closeLoginOnBack")} ${arguments?.toString()}")
        arguments?.getBoolean("closeLoginOnBack", false)?.let {
            Timber.e("closeLoginOnBack read as $it")
            closeLoginOnBack = it
        }
    }

    override fun onResume() {
        super.onResume()
        if(shouldCheckMergeAccountOnResume)
        {
            onCheckMergeAccountStatus()
            shouldCheckMergeAccountOnResume = false
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

        webView.settings.javaScriptEnabled = true
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
        webView.addJavascriptInterface(WebAppInterface(), "app")
    }

    abstract fun onOverrideUrlLoading(view: WebView?, url: String?) : Boolean
    abstract fun onLoadFinished(view: WebView?, url: String?)
    open fun loginKspToken(kspwebtoken : String) {}
    open fun onCheckMergeAccountStatus() {}

    private inner class WebAppInterface {
        @JavascriptInterface
        fun logon(kspweb: String, ticket: String) {
            Timber.d("Received logon url from webview: kspweb=$kspweb, ticket=$ticket")
            //onReceivedWebLogin(kspweb, ticket)
            loginKspToken(ticket)
        }


        @JavascriptInterface
        fun performAppSwitch() {
            Timber.e("Perform ipswitch")
        }

    }

    fun showMergeAcountDrawer() {
        shouldCheckMergeAccountOnResume = true
        getBaseActivity()?.openComponentDrawer(MergeAccountComponentFragment::class.java)
    }
}