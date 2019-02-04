package dk.eboks.app.presentation.ui.channels.components.content.web

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseWebFragment
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentFragment
import kotlinx.android.synthetic.main.fragment_base_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.json.JSONObject
import timber.log.Timber
import java.net.URLEncoder
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelContentComponentFragment : BaseWebFragment(), ChannelContentComponentContract.View {

    @Inject
    lateinit var presenter: ChannelContentComponentContract.Presenter

    var channelAppInterface: ChannelContentComponentFragment.ChannelAppInterface? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)

        presenter.onViewCreated(this, lifecycle)

        refreshSrl.isEnabled = true
        refreshSrl.setOnRefreshListener {
            // todo user refreshed the webview
            // todo when we know what to refresh, add some BETTER (lol) logic to stop the refreshing
            Timber.e("Reloading web content")
            webView.reload()

            // fake it :/
            refreshSrl.postDelayed({
                refreshSrl.isRefreshing = false
            }, 200)
        }

        arguments?.getParcelable<Channel>(Channel::class.simpleName)?.let {
            presenter.setup(it)
            setupTopBar()
        }
    }

    // looks like this "mobilepay://send?amount=100&phone=24770011" taste like crab
    fun openMobilePay(url: String): Boolean {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity?.packageManager?.let {
            return if (i.resolveActivity(it) != null) {
                startActivity(i)
                true
            } else {
                Timber.d("No Intent available to handle action")
                false
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        getBaseActivity()?.backPressedCallback = {
            if (webView.canGoBack())
                webView.goBack()
            else
                activity?.finish()
            true
        }
    }

    override fun onPause() {
        getBaseActivity()?.backPressedCallback = null
        super.onPause()
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            if (webView.canGoBack())
                webView.goBack()
            else
                activity?.finish()
        }

        val menuItem = mainTb.menu.add("_settings")
        menuItem.setIcon(R.drawable.ic_settings_red)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.setOnMenuItemClickListener { item: MenuItem ->
            val args = Bundle()
            args.putParcelable(Channel::class.java.simpleName, presenter.currentChannel)
            getBaseActivity()?.openComponentDrawer(
                ChannelSettingsComponentFragment::class.java,
                args
            )
            true
        }
    }

    override fun showChannel(channel: Channel) {
        mainTb.title = channel.name
        channelAppInterface = ChannelAppInterface(this, webView)
        webView.addJavascriptInterface(channelAppInterface, "android")
    }

    override fun openChannelContent(content: String) {
        // val url = "file:///android_asset/index.html"
        if (content.isBlank()) {
            webView.loadData("Missing channel content link", "text/html", "utf8")
            return
        }
        webView.loadData(content, "text/html", "utf8")
    }

    override fun onOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        // mobile pay app deeplinking support (if present on device)
        url?.let {
            if (it.startsWith("mobilepay://")) {
                val result = openMobilePay(url)
                if (!result) // if mobile pay not installed on device
                {
                    AlertDialog.Builder(context ?: return@let)
                        .setTitle(Translation.mobilepaysupport.mobilePayNotInstalledTitle)
                        .setMessage(Translation.mobilepaysupport.mobilePayNotInstalledMessage)
                        .setPositiveButton(Translation.mobilepaysupport.installMobilePayBtn.toUpperCase()) { dialog, which ->
                            val appPackageName = "dk.danskebank.mobilepay"
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$appPackageName")
                                    )
                                )
                            } catch (anfe: android.content.ActivityNotFoundException) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                    )
                                )
                            }
                        }
                        .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                        }
                        .create()
                        .show()
                }
                return true
            }
            if (it.startsWith("mailto:")) {
                channelAppInterface?.let {
                    it.email(url.substringAfter("mailto:"), "", "")
                }
                return true
            }
            if (it.startsWith("tel:")) {
                channelAppInterface?.let {
                    it.call(url)
                }
                return true
            }
        }
        return false
    }

    override fun onLoadFinished(view: WebView?, url: String?) {
    }

    class ChannelAppInterface(private val baseFragment: BaseWebFragment, val webView: WebView) {
        // output functions
        private fun updateUser(data: String) {
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
                Toast.makeText(
                    baseFragment.context,
                    Translation.error.noInternetTitle,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        @JavascriptInterface
        fun send(sendKey: String, sendValue: String) {
            Timber.v("Send: {$sendKey, $sendValue}")
            Toast.makeText(baseFragment.context, "{$sendKey, $sendValue}", Toast.LENGTH_SHORT)
                .show()
        }

        @JavascriptInterface
        fun retrieve(retrieveKey: String) {
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
                baseFragment.startActivity(
                    Intent.createChooser(
                        intent,
                        Translation.signup.emailHint
                    )
                )
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    baseFragment.context,
                    Translation.error.noInternetTitle,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        @JavascriptInterface
        fun pushWebpage(uri: String) { // Same as presentWebpage(), but perhaps with different animation
            Timber.v("PushWebPage: $uri")
            val fragment = ChannelContentComponentFragment()
            val args = Bundle()
            args.putString(
                Channel::class.simpleName,
                "file:///android_asset/index2.html"
            ) // TODO: remove me and use the URI instead
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
        fun map(name: String, address: String) {
            val search = "geo:0,0?q=" + URLEncoder.encode("$name+$address", "UTF-8")

            Timber.v("Map: $name, $address = $search")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(search)
            baseFragment.context?.packageManager?.let {
                if (intent.resolveActivity(it) != null) {
                    baseFragment.startActivity(intent)
                }
            }
        }
    }
}