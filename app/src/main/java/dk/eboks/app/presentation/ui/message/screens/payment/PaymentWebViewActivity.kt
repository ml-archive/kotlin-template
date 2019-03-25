package dk.eboks.app.presentation.ui.message.screens.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import dk.eboks.app.R
import dk.eboks.app.domain.models.message.payment.PaymentCallback
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.BaseFragment
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_payment_web_view.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber

class PaymentWebViewActivity : BaseActivity() {

    private val link: Link?
        get() = intent?.getParcelableExtra(EXTRA_PAYMENT_LINK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_web_view)
        link?.let(this::setupWebView)
        setupToolbar()
    }

    private fun setupToolbar() {
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
    }

    private fun setupWebView(link: Link) {
        paymentWebView.settings.javaScriptEnabled = true
        paymentWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                Timber.d("Loaded: $url")
                url?.let { handleUrlUpdate(it) }
            }
        }

        paymentWebView.webChromeClient = object : WebChromeClient() {
        }

        paymentWebView.loadUrl(link.url)
    }

    private fun handleUrlUpdate(url: String) {
        when (url) {
            PaymentCallback.CANCEL.url -> {
                showToast("cancel")
            }
            PaymentCallback.FAILURE.url -> {
                showToast("fail")
            }
            PaymentCallback.SUCCESS.url -> {
                showPaymentComplete()
            }
            else -> {
            }
        }
    }

    private fun showPaymentComplete() {
        paymentDoneCl.isVisible = true
        paymentWebView.isVisible = false

        doneButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        mainTb.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    companion object {

        private const val EXTRA_PAYMENT_LINK = "PaymentLink"
        const val REQUEST_CODE = 44

        fun startForResult(fragment: BaseFragment, link: Link) {
            val intent = Intent(fragment.context, PaymentWebViewActivity::class.java).apply {
                putExtra(EXTRA_PAYMENT_LINK, link)
            }
            fragment.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}
