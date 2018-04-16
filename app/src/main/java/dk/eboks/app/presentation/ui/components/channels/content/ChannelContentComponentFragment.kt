package dk.eboks.app.presentation.ui.components.channels.content

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseWebFragment
import dk.eboks.app.presentation.ui.components.channels.requirements.ChannelRequirementsComponentFragment
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import kotlinx.android.synthetic.main.fragment_base_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.json.JSONObject
import javax.inject.Inject
import timber.log.Timber
import android.support.v4.content.ContextCompat.startActivity
import java.net.URLEncoder


/**
 * Created by bison on 09-02-2018.
 */
class ChannelContentComponentFragment : BaseWebFragment(), ChannelContentComponentContract.View {

    @Inject
    lateinit var presenter : ChannelContentComponentContract.Presenter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)

        presenter.onViewCreated(this, lifecycle)

        Handler(Looper.getMainLooper()).post({
            getBaseActivity()?.openComponentDrawer(ChannelRequirementsComponentFragment::class.java)
        })
        setupTopBar()
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            activity.onBackPressed()
        }

        val menuSearch = mainTb.menu.add("_settings")
        menuSearch.setIcon(R.drawable.ic_settings_red)
        menuSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuSearch.setOnMenuItemClickListener { item: MenuItem ->
            getBaseActivity()?.openComponentDrawer(ChannelSettingsComponentFragment::class.java)
            true
        }

    }

    override fun showChannel(channel: Channel) {
        mainTb.title = channel.name

        webView.addJavascriptInterface(ChannelAppInterface(this, webView), "android")

        val url = arguments.getString(Channel::class.simpleName)
        if(url.isNullOrBlank()) {
            webView.loadData("Channel content webview - not implemented", "text/html", "utf8")
        }
        webView.loadUrl(url)
    }

    override fun onOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }

    override fun onLoadFinished(view: WebView?, url: String?) {

    }

    class ChannelAppInterface (private val baseFragment : BaseWebFragment, val webView: WebView) {
        // output functions
        private fun updateUser(data : String) {
            val jUser = JSONObject()
            jUser.put("username", data)
            webView.post {
                webView.evaluateJavascript("updateUser($jUser)", null)
            }
        }
        private fun displayValue(value: String) {
            val jData = JSONObject()
            jData.put("value", value)
            webView.post {
                webView.evaluateJavascript("displayValue($jData)", null)
            }
        }

        // input functions
        @JavascriptInterface
        fun login(msg: String) {
            Timber.v("Login: $msg")
            updateUser("Testa Trialson")
        }
        @JavascriptInterface
        fun share(shareText: String) {
            Timber.v("Share: $shareText")

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, shareText)
            try {
                baseFragment.startActivity(Intent.createChooser(intent, "Share"))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(baseFragment.context, Translation.error.noInternetTitle, Toast.LENGTH_SHORT).show()
            }
        }
        @JavascriptInterface
        fun send(sendKey: String, sendValue: String) {
            Timber.v("Send: {$sendKey, $sendValue}")
            Toast.makeText(baseFragment.context, "{$sendKey, $sendValue}", Toast.LENGTH_SHORT).show()
        }
        @JavascriptInterface
        fun retrieve(retrieveKey : String) {
            Timber.v("Retrieve: $retrieveKey")
            val retrieveValue = "$retrieveKey -  Hello world!"
            displayValue(retrieveValue)
        }
        @JavascriptInterface
        fun call(uri: String) {
            Timber.v("Call: $uri")
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            baseFragment.startActivity(intent)
        }
        @JavascriptInterface
        fun email(address: String, subject: String, body: String) {
            Timber.v("Email: $address, $subject, $body")
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, body)
            try {
                baseFragment.startActivity(Intent.createChooser(intent, Translation.signup.emailHint))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(baseFragment.context, Translation.error.noInternetTitle, Toast.LENGTH_SHORT).show()
            }
        }
        @JavascriptInterface
        fun pushWebpage(uri: String) {
            Timber.v("PushWebPage: $uri")
            val fragment = ChannelContentComponentFragment()
            val args = Bundle()
            args.putString(Channel::class.simpleName, "file:///android_asset/index2.html")
            fragment.arguments = args
            baseFragment.getBaseActivity()?.addFragmentOnTop(R.id.content, fragment, true)
        }
        @JavascriptInterface
        fun map(name : String, address : String) {
            val search = "geo:0,0?q=" + URLEncoder.encode("$name+$address", "UTF-8")

            Timber.v("Map: $name, $address = $search")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(search)
            if (intent.resolveActivity(baseFragment.context.packageManager) != null) {
                baseFragment.startActivity(intent)
            }
        }
    }
}