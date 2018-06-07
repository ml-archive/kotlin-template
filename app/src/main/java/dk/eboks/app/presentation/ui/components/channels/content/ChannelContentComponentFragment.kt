package dk.eboks.app.presentation.ui.components.channels.content

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.base.BaseWebFragment
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import kotlinx.android.synthetic.main.fragment_base_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.json.JSONObject
import javax.inject.Inject
import timber.log.Timber
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

        refreshSrl.isEnabled = true
        refreshSrl.setOnRefreshListener {
            //todo user refreshed the webview
            //todo when we know what to refresh, add some logic to stop the refreshing
            var temp = "_refresh view"
            Timber.e(temp)
            webView.reload()
        }

        setupTopBar()

        arguments?.getSerializable(Channel::class.simpleName)?.let {
            presenter.setup(it as Channel)
        }
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            activity.onBackPressed()
        }

        val menuItem = mainTb.menu.add("_settings")
        menuItem.setIcon(R.drawable.ic_settings_red)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.setOnMenuItemClickListener { item: MenuItem ->
            getBaseActivity()?.openComponentDrawer(ChannelSettingsComponentFragment::class.java)
            true
        }
    }

    override fun showChannel(channel: Channel) {
        mainTb.title = channel.name
        webView.addJavascriptInterface(ChannelAppInterface(this, webView), "android")
    }

    override fun openChannelLink(link: Link) {
        //val url = "file:///android_asset/index.html"
        if(link.url.isNullOrBlank()) {
            webView.loadData("Missing channel content link", "text/html", "utf8")
        }
        webView.loadUrl(link.url)
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
        fun pushWebpage(uri: String) { // Same as presentWebpage(), but perhaps with different animation
            Timber.v("PushWebPage: $uri")
            val fragment = ChannelContentComponentFragment()
            val args = Bundle()
            args.putString(Channel::class.simpleName, "file:///android_asset/index2.html") // TODO: remove me and use the URI instead
//            args.putString(Channel::class.simpleName, uri)
            fragment.arguments = args
            baseFragment.getBaseActivity()?.addFragmentOnTop(R.id.content, fragment, true)
        }
        @JavascriptInterface
        fun presentWebpage(uri: String) { // Same as pushWebpage(), but perhaps with different animation
            Timber.v("presentWebpage: $uri")
            val fragment = ChannelContentComponentFragment()
            val args = Bundle()
            args.putString(Channel::class.simpleName, uri)
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