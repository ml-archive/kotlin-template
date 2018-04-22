package dk.eboks.app.presentation.ui.components.channels.content

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
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptItem
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.StoreboxContentActivity
import kotlinx.android.synthetic.main.fragment_channel_storebox_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

class ChannelContentStoreboxComponentFragment : BaseFragment(),
                                                ChannelContentStoreboxComponentContract.View {
    @Inject
    lateinit var formatter: EboksFormatter
    @Inject
    lateinit var presenter: ChannelContentStoreboxComponentContract.Presenter

    private var adapter = StoreboxAdapter()

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater?.inflate(
                R.layout.fragment_channel_storebox_component,
                container,
                false
        )
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setup()
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

    private fun setup() {
        receiptRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        receiptRv.adapter = adapter
    }

    private fun onBackPressed() {
        getBaseActivity()?.onBackPressed()
    }

    override fun setReceipts(data: ArrayList<StoreboxReceiptItem>) {
        Timber.d("setReceipts: %s", data.size)
        adapter.receipts = data
        adapter.notifyDataSetChanged()
    }

    inner class StoreboxAdapter : RecyclerView.Adapter<StoreboxAdapter.StoreboxViewHolder>() {
        var receipts: MutableList<StoreboxReceiptItem> = ArrayList()

        inner class StoreboxViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
            //cards
            var amountDateContainer = root.findViewById<LinearLayout>(R.id.amountDateContainerLl)
            var soloAmountTv = root.findViewById<TextView>(R.id.soloAmountTv)
            val row = root.findViewById<LinearLayout>(R.id.rowLl)
            val headerTv = root.findViewById<TextView>(R.id.headerTv)
            val amountTv = root.findViewById<TextView>(R.id.amountTv)
            val dateTv = root.findViewById<TextView>(R.id.dateTv)
            val logoIv = root.findViewById<ImageView>(R.id.logoIv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreboxViewHolder {
            val v = LayoutInflater.from(context).inflate(
                    R.layout.viewholder_channel_storebox_row,
                    parent,
                    false
            )
            val vh = StoreboxViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return receipts.size
        }

        override fun onBindViewHolder(holder: StoreboxViewHolder?, position: Int) {
            var currentReceipt = receipts[position]

            holder?.headerTv?.text = currentReceipt.storeName

            if (currentReceipt.purchaseDate != null) {
                holder?.amountDateContainer?.visibility = View.VISIBLE
                holder?.soloAmountTv?.visibility = View.GONE

                holder?.amountTv?.text = String.format("%.2f", currentReceipt.grandTotal?.value).replace(".",",")
                holder?.dateTv?.text = formatter.formatDateRelative(currentReceipt)
            } else {
                holder?.amountDateContainer?.visibility = View.GONE
                holder?.soloAmountTv?.visibility = View.VISIBLE

                holder?.amountTv?.text = String.format("%.2f", currentReceipt.grandTotal?.value).replace(".",",")            }
            if (currentReceipt.logo?.url != null) {
                holder?.logoIv?.let {
                    Glide.with(context).load(currentReceipt.logo?.url).into(it)
                }
            }

            holder?.row?.setOnClickListener {
                //todo open the receipt details
                Timber.d("Receipt Clicked: %s", currentReceipt.id)
                (activity as StoreboxContentActivity).showDetailFragment(currentReceipt.id)
            }

        }

    }
}