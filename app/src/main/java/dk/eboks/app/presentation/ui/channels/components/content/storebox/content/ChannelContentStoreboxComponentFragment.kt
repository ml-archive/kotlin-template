package dk.eboks.app.presentation.ui.channels.components.content.storebox.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptItem
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.components.content.storebox.detail.ChannelContentStoreboxDetailComponentFragment
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentFragment
import dk.eboks.app.util.inflate
import dk.eboks.app.util.putArg
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_channel_storebox_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.viewholder_channel_storebox_row.view.*
import timber.log.Timber
import javax.inject.Inject

class ChannelContentStoreboxComponentFragment : BaseFragment(),
    ChannelContentStoreboxComponentContract.View {
    @Inject
    lateinit var formatter: EboksFormatter
    @Inject
    lateinit var presenter: ChannelContentStoreboxComponentContract.Presenter

    private var adapter = StoreboxAdapter()

    private var channel: Channel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel_storebox_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setup()
        setupTopbar()

        arguments?.getParcelable<Channel>(Channel::class.java.simpleName)?.let {
            channel = it
        }

        addCreditCardsBtn.setOnClickListener {
            val arguments = Bundle()
            arguments.putCharSequence("arguments", "storebox")
            arguments.putBoolean("openAddCreditCards", true)
            arguments.putParcelable(Channel::class.java.simpleName, channel)
            getBaseActivity()?.openComponentDrawer(
                ChannelSettingsComponentFragment::class.java,
                arguments
            )
        }
        showProgress(true)
    }

    private fun setupTopbar() {
        Timber.e("Running setupTopbar")
        mainTb?.menu?.clear()

        mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb?.setNavigationOnClickListener {
            onBackPressed()
        }

        val menuSearch = mainTb?.menu?.add("_settings")
        menuSearch?.setIcon(R.drawable.ic_settings_red)
        menuSearch?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuSearch?.setOnMenuItemClickListener { item: MenuItem ->
            val arguments = Bundle()
            arguments.putCharSequence("arguments", "storebox")
            arguments.putParcelable(Channel::class.java.simpleName, channel)
            getBaseActivity()?.openComponentDrawer(
                ChannelSettingsComponentFragment::class.java,
                arguments
            )
            true
        }
        mainTb.title = Translation.storeboxlist.title
    }

    private fun setup() {
        receiptRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        receiptRv.adapter = adapter
    }

    private fun onBackPressed() {
        getBaseActivity()?.onBackPressed()
    }

    override fun showProgress(show: Boolean) {
        Timber.d("Show Progress View: %s", show)

        storagePb.isVisible = show
        receiptRv.isVisible = !show
        containerEmpty.isVisible = false
        noCreditCardEmptyLl.isVisible = false
    }

    override fun showEmptyView(show: Boolean) {
        Timber.d("Show Empty View: %s", show)
        receiptRv.isVisible = !show
        storagePb.isVisible = false
        containerEmpty.isVisible = show
        noCreditCardEmptyLl.isVisible = show
    }

    override fun showNoCreditCardsEmptyView(show: Boolean) {
        receiptRv.isVisible = !show
        storagePb.isVisible = false
        containerEmpty.isVisible = show
        noCreditCardEmptyLl.isVisible = show
    }

    override fun setReceipts(data: List<StoreboxReceiptItem>) {
        Timber.d("setReceipts: %s", data.size)

        adapter.receipts.clear()
        adapter.receipts.addAll(data)
        adapter.notifyDataSetChanged()

        showEmptyView(data.isEmpty())
    }

    inner class StoreboxAdapter : RecyclerView.Adapter<StoreboxAdapter.StoreboxViewHolder>() {
        var receipts: MutableList<StoreboxReceiptItem> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreboxViewHolder {
            return StoreboxViewHolder(parent.inflate(R.layout.viewholder_channel_storebox_row))
        }

        override fun getItemCount(): Int {
            return receipts.size
        }

        override fun onBindViewHolder(holder: StoreboxViewHolder, position: Int) {
            val currentReceipt = receipts[position]
            holder.bind(currentReceipt)
        }

        inner class StoreboxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(currentReceipt: StoreboxReceiptItem) {
                itemView.run {
                    headerTv?.text = currentReceipt.storeName

                    if (currentReceipt.purchaseDate != null) {
                        amountDateContainerLl?.visibility = View.VISIBLE
                        soloAmountTv?.visibility = View.GONE
                        dateTv?.text = formatter.formatDateRelative(currentReceipt)
                        Timber.e("Date: ${dateTv.text}")
                    } else {
                        amountDateContainerLl?.visibility = View.GONE
                        soloAmountTv?.visibility = View.VISIBLE
                    }
                    amountTv?.text = String.format(
                        "%.2f",
                        currentReceipt.grandTotal
                    )
                    if (currentReceipt.logo?.url != null) {
                        logoIv?.let {
                            Glide.with(itemView.context).load(currentReceipt.logo?.url).into(it)
                        }
                    }

                    rowLl?.setOnClickListener {
                        Timber.d("Receipt Clicked: %s", currentReceipt.id)
                        addFragmentOnTop(
                            R.id.content,
                            ChannelContentStoreboxDetailComponentFragment().putArg(
                                StoreboxReceipt.KEY_ID!!,
                                currentReceipt.id
                            ),
                            true
                        )
                    }
                }
            }
        }
    }
}