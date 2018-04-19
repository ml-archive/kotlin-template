package dk.eboks.app.presentation.ui.components.channels.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.StoreboxContentActivity
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class ChannelContentStoreboxDetailComponentFragment : BaseFragment(),
                                                      ChannelContentStoreboxDetailComponentContract.View {
    @Inject
    lateinit var formatter: EboksFormatter
    @Inject
    lateinit var presenter: ChannelContentStoreboxDetailComponentContract.Presenter

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(
                R.layout.fragment_channel_storebox_detail_component,
                container,
                false
        )
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopbar()
    }

    private fun setupTopbar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            onBackPressed()
        }

        val menuSearch = getBaseActivity()?.mainTb?.menu?.add("_settings")
        menuSearch?.setIcon(R.drawable.ic_settings_red)
        menuSearch?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuSearch?.setOnMenuItemClickListener { item: MenuItem ->
            val arguments = Bundle()
            arguments.putCharSequence("arguments", "storebox")
            getBaseActivity()?.openComponentDrawer(
                    ChannelSettingsComponentFragment::class.java,
                    arguments
            )
            true
        }
    }

    private fun onBackPressed() {
        (activity as StoreboxContentActivity).goToRoot()
    }

    override fun getReceiptId(): String? {
        return arguments?.getString(StoreboxReceipt.KEY_ID)
    }

    override fun setReceipt(receipt: StoreboxReceipt) {
        Timber.d("Setting Receipt: %s", receipt)
    }
}