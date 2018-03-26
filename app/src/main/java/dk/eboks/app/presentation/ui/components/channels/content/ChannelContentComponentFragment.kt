package dk.eboks.app.presentation.ui.components.channels.content

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseWebFragment
import dk.eboks.app.presentation.ui.components.channels.requirements.ChannelRequirementsComponentFragment
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import kotlinx.android.synthetic.main.fragment_base_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

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
        webView.loadData("Channel content webview - not implemented", "text/html", "utf8")

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
    }

    override fun onOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }

    override fun onLoadFinished(view: WebView?, url: String?) {

    }
}