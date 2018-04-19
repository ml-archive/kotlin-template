package dk.eboks.app.presentation.ui.components.channels.content

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.channel.storebox.StoreboxMerchant
import dk.eboks.app.domain.models.channel.storebox.StoreboxOptionals
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptLine
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.StoreboxContentActivity
import dk.eboks.app.util.setVisible
import kotlinx.android.synthetic.main.fragment_channel_storebox_detail_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.viewholder_receipt_line.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ChannelContentStoreboxDetailComponentFragment : BaseFragment(),
                                                      ChannelContentStoreboxDetailComponentContract.View {
    @Inject
    lateinit var formatter: EboksFormatter
    @Inject
    lateinit var presenter: ChannelContentStoreboxDetailComponentContract.Presenter

    private var adapter = ReceiptLineAdapter()

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
        showProgress(true)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupTopbar()
        setupRecycler()
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

    private fun setupRecycler() {
        storeboxDetailRvReceiptLines.layoutManager = LinearLayoutManager(context)
        storeboxDetailRvReceiptLines.adapter = adapter

        ViewCompat.setNestedScrollingEnabled(storeboxDetailRvReceiptLines, false)
    }

    private fun onBackPressed() {
        (activity as StoreboxContentActivity).goToRoot()
    }

    override fun getReceiptId(): String? {
        return arguments?.getString(StoreboxReceipt.KEY_ID)
    }

    override fun showProgress(isLoading: Boolean) {
        storeboxDetailProgressBar.setVisible(isLoading)
        storeboxDetailContentContainer.setVisible(!isLoading)
    }

    override fun setReceipt(receipt: StoreboxReceipt) {
        Timber.d("Setting Receipt: %s", receipt)

        receipt.receiptLines
        setStoreInfo(receipt.merchant, receipt.optionals)
        setReceiptDate(receipt.purchaseDateTime ?: Date(), receipt.optionals)
        setLogo(receipt.merchant?.logo?.url ?: "")
        setReceiptLines(receipt.receiptLines)

        showProgress(false)
    }

    private fun setLogo(url: String?) {
        if (url == null) {
            return
        }

        val requestOptions = RequestOptions()
        requestOptions.fitCenter()
        requestOptions.override(300, 300)

        Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(storeboxDetailIvLogo)
    }

    private fun setReceiptDate(date: Date, optionals: StoreboxOptionals?) {
        storeboxDetailTvDate.text = formatter.formatDateToDay(date)
        storeboxDetailTvTime.text = formatter.formatDateToTime(date)
        storeboxDetailTvOrderNumber.text = optionals?.orderNumber
    }

    private fun setStoreInfo(merchant: StoreboxMerchant?, optionals: StoreboxOptionals?) {
        storeboxDetailTvStoreName.text = merchant?.name
        storeboxDetailTvAddressLineOne.text = merchant?.addressLine1
        storeboxDetailTvAddressLineTwo.text = merchant?.addressLine2
        storeboxDetailTvPhoneNumber.text = optionals?.storeRegNumber
    }

    private fun setReceiptLines(data: ArrayList<StoreboxReceiptLine>) {
        adapter.receiptLines = data
        adapter.notifyDataSetChanged()
    }

    inner class ReceiptLineAdapter : RecyclerView.Adapter<ReceiptLineAdapter.ReceiptLineViewHolder>() {
        var receiptLines: ArrayList<StoreboxReceiptLine> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptLineViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.viewholder_receipt_line,
                    parent,
                    false
            )
            return ReceiptLineViewHolder(view)
        }

        override fun getItemCount(): Int {
            return receiptLines.size
        }

        override fun onBindViewHolder(holder: ReceiptLineViewHolder, position: Int) {
            val receiptLine = receiptLines[position]
            holder.bind(receiptLine)
        }

        inner class ReceiptLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(receiptLine: StoreboxReceiptLine) {
                itemView.viewHolderReceiptTvItemName.text = receiptLine.name

                itemView.viewHolderReceiptTvAmount.text = String.format(
                        "%s x %.2f",
                        receiptLine.amount?.toInt(),
                        receiptLine.itemPrice?.value
                )

                itemView.viewHolderReceiptTvPrice.text = receiptLine.totalPrice?.value.toString()

                itemView.viewHolderReceiptTvSubtitle.setVisible(receiptLine.description != null)
                itemView.viewHolderReceiptTvSubtitle.text = receiptLine.description
            }
        }
    }
}