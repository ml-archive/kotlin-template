package dk.eboks.app.presentation.ui.message.screens.payment

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import dk.eboks.app.R
import dk.eboks.app.domain.models.message.payment.PaymentOption
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.visible
import kotlinx.android.synthetic.main.activity_payment_web_view.*
import kotlinx.android.synthetic.main.include_toolbar.*

class PaymentWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_web_view)
        setupWebView()
        setupToolbar()

    }


    private fun setupToolbar() {
        mainTb.setNavigationOnClickListener {
           onBackPressed()
        }
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
    }

    private fun setupWebView() {
        paymentWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
               url?.let {
                   if (!it.contains("google")) {
                       showPaymentComplete()
                   }
               }
            }
        }

        paymentWebView.webChromeClient = object : WebChromeClient() {

        }

        paymentWebView.loadUrl("https://google.com")

    }

    private fun showPaymentComplete() {
        paymentDoneCl.visible = true
        paymentWebView.visible = false

        doneButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        mainTb.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {

        private const val EXTRA_PAYMENT = "PaymentOption"
        const val REQUEST_CODE = 44

        fun startForResult(fragment: BaseFragment, paymentOption: PaymentOption) {
            val intent = Intent(fragment.context, PaymentWebViewActivity::class.java).apply {
                putExtra(EXTRA_PAYMENT, paymentOption)
            }
            fragment.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}
