package dk.eboks.app.presentation.ui.channels.components.content.storebox.content

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import dk.eboks.app.util.putArg
import dk.eboks.app.util.setVisible
import kotlinx.android.synthetic.main.fragment_channel_storebox_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class ChannelContentStoreboxComponentFragment : BaseFragment(),
                                                ChannelContentStoreboxComponentContract.View {
    @Inject
    lateinit var formatter: EboksFormatter
    @Inject
    lateinit var presenter: ChannelContentStoreboxComponentContract.Presenter

    private var adapter = StoreboxAdapter()

    private var channel : Channel? = null

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(
                R.layout.fragment_channel_storebox_component,
                container,
                false
        )
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setup()
        setupTopbar()

        arguments?.getSerializable(Channel::class.java.simpleName)?.let {
            channel = it as Channel
        }

        addCreditCardsBtn.setOnClickListener {
            val arguments = Bundle()
            arguments.putCharSequence("arguments", "storebox")
            arguments.putBoolean("openAddCreditCards", true)
            arguments.putSerializable(Channel::class.java.simpleName, channel)
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
            arguments.putSerializable(Channel::class.java.simpleName, channel)
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

        progressBar.setVisible(show)
        receiptRv.setVisible(!show)
        containerEmpty.setVisible(false)
        noCreditCardEmptyLl.setVisible(false)
    }

    override fun showEmptyView(show: Boolean) {
        Timber.d("Show Empty View: %s", show)
        receiptRv.setVisible(!show)
        progressBar.setVisible(false)
        containerEmpty.setVisible(show)
        noCreditCardEmptyLl.setVisible(show)
    }

    override fun showNoCreditCardsEmptyView(show: Boolean) {
        receiptRv.setVisible(!show)
        progressBar.setVisible(false)
        containerEmpty.setVisible(show)
        noCreditCardEmptyLl.setVisible(show)
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
            val v = LayoutInflater.from(context).inflate(
                    R.layout.viewholder_channel_storebox_row,
                    parent,
                    false
            )
            return StoreboxViewHolder(v)
        }

        override fun getItemCount(): Int {
            return receipts.size
        }

        override fun onBindViewHolder(holder: StoreboxViewHolder?, position: Int) {
            val currentReceipt = receipts[position]
            holder?.bind(currentReceipt)
        }

        inner class StoreboxViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
            //cards
            private var amountDateContainer = root.findViewById<LinearLayout>(R.id.amountDateContainerLl)
            private var soloAmountTv = root.findViewById<TextView>(R.id.soloAmountTv)
            private val row = root.findViewById<LinearLayout>(R.id.rowLl)
            private val headerTv = root.findViewById<TextView>(R.id.headerTv)
            private val amountTv = root.findViewById<TextView>(R.id.amountTv)
            private val dateTv = root.findViewById<TextView>(R.id.dateTv)
            private val logoIv = root.findViewById<ImageView>(R.id.logoIv)

            fun bind(currentReceipt: StoreboxReceiptItem) {
                headerTv?.text = currentReceipt.storeName

                if (currentReceipt.purchaseDate != null) {
                    amountDateContainer?.visibility = View.VISIBLE
                    soloAmountTv?.visibility = View.GONE
                    dateTv?.text = formatter.formatDateRelative(currentReceipt)
                } else {
                    amountDateContainer?.visibility = View.GONE
                    soloAmountTv?.visibility = View.VISIBLE
                }
                amountTv?.text = String.format(
                        "%.2f",
                        currentReceipt.grandTotal
                )
                if (currentReceipt.logo?.url != null) {
                    logoIv?.let {
                        Glide.with(context).load(currentReceipt.logo?.url).into(it)
                    }
                }

                row?.setOnClickListener {
                    Timber.d("Receipt Clicked: %s", currentReceipt.id)
                    addFragmentOnTop(R.id.content, ChannelContentStoreboxDetailComponentFragment().putArg(StoreboxReceipt.KEY_ID!!, currentReceipt.id) as BaseFragment, true)
                }
            }
        }
    }
}