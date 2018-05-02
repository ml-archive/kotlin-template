package dk.eboks.app.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import dk.eboks.app.R
import kotlinx.android.synthetic.main.fragment_base_web.*

/**
 * Created by bison on 13/03/2018.
 */
abstract class BaseWebFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_base_web, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshSrl.isEnabled = false
        setupWebView()
    }

    fun setupWebView()
    {
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

    abstract fun onOverrideUrlLoading(view: WebView?, url: String?) : Boolean
    abstract fun onLoadFinished(view: WebView?, url: String?)
}