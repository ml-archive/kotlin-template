package dk.eboks.app.presentation.ui.channels.components.content.storebox.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.storebox.*
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.folder.screens.FolderActivity
import dk.eboks.app.presentation.ui.overlay.screens.ButtonType
import dk.eboks.app.presentation.ui.overlay.screens.OverlayActivity
import dk.eboks.app.presentation.ui.overlay.screens.OverlayButton
import dk.eboks.app.util.setVisible
import kotlinx.android.synthetic.main.fragment_channel_storebox_detail_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.viewholder_payment_line.view.*
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
    private var paymentAdapter = PaymentLineAdapter()

    private var actionButtons = arrayListOf(
            OverlayButton(ButtonType.MOVE),
            OverlayButton(ButtonType.DELETE)
    )

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
        setupRecyclers()
        presenter.loadReceipt()
    }

    private fun setupTopbar() {
        mainTb?.menu?.clear()

        mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb?.setNavigationOnClickListener {
            fragmentManager.popBackStack()
        }
        
        //mainTb?.overflowIcon = context.resources.getDrawable(R.drawable.icon_48_option_red_navigationbar)

        val menuItem = mainTb?.menu?.add("_options")
        menuItem?.setIcon(R.drawable.icon_48_option_red_navigationbar)
        menuItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem?.setOnMenuItemClickListener { item: MenuItem ->
            val i = Intent(context, OverlayActivity::class.java)
            i.putExtra("buttons", actionButtons)
            startActivityForResult(i, OverlayActivity.REQUEST_ID)
            true
        }
    }

    private fun setupRecyclers() {
        storeboxDetailRvReceiptLines.layoutManager = LinearLayoutManager(context)
        storeboxDetailRvReceiptLines.adapter = adapter

        ViewCompat.setNestedScrollingEnabled(storeboxDetailRvReceiptLines, false)

        storeboxDetailRvPayments.layoutManager = LinearLayoutManager(context)
        storeboxDetailRvPayments.adapter = paymentAdapter

        ViewCompat.setNestedScrollingEnabled(storeboxDetailRvPayments, false)
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

        mainTb?.title = receipt.merchant?.name ?: ""
        setStoreInfo(receipt.merchant, receipt.optionals)
        setReceiptDate(receipt.purchaseDateTime ?: Date(), receipt.optionals)
        setLogo(receipt.merchant?.logo?.url ?: "")
        setReceiptAmount(receipt.grandTotal)
        setReceiptLines(receipt.receiptLines.items)
        setPayments(receipt.payments)
        setOptionals(receipt.optionals)
        setBarcode(receipt.barcode)

        showProgress(false)

    }

    private fun setLogo(url: String?) {
        if (url == null) {
            storeboxDetailImageContainer.setVisible(false)
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

        storeboxDetailTvOrderNumber.text = String.format(
                "%s %s",
                Translation.storeboxreceipt.orderNo,
                optionals?.orderNumber
        )
    }

    private fun setStoreInfo(merchant: StoreboxMerchant?, optionals: StoreboxOptionals?) {
        storeboxDetailTvStoreName.text = merchant?.name
        storeboxDetailTvAddressLineOne.text = merchant?.addressLine1
        storeboxDetailTvAddressLineTwo.text = merchant?.addressLine2
        storeboxDetailTvPhoneNumber.text = optionals?.storeRegNumber
    }

    private fun setReceiptAmount(grandTotal: StoreboxReceiptPrice?) {
        if (grandTotal == null) {
            return
        }

        storeboxDetailTvTotal.text = grandTotal.value.toString()
        storeboxDetailTvVat.text = grandTotal.vat.toString()
    }

    private fun setReceiptLines(data: ArrayList<StoreboxReceiptLine>) {
        adapter.receiptLines = data
        adapter.notifyDataSetChanged()
    }

    private fun setPayments(data: ArrayList<StoreboxPayment>) {
        paymentAdapter.payments = data
        paymentAdapter.notifyDataSetChanged()
    }

    private fun setOptionals(optionals: StoreboxOptionals?) {
        if (optionals == null) {
            return
        }

        val headerIsValid = !optionals.footerText.isNullOrEmpty()
        val footerIsValid = !optionals.footerText.isNullOrEmpty()

        if (!headerIsValid && !footerIsValid) {
            return
        }

        storeboxDetailOptionalsContainer.setVisible(true)

        if (headerIsValid) {
            storeboxDetailTvOptionalHeader.text = optionals.headerText
        } else {
            storeboxDetailTvOptionalHeader.setVisible(false)
        }

        if (footerIsValid) {
            storeboxDetailTvOptionalFooter.text = optionals.footerText
        } else {
            storeboxDetailTvOptionalFooter.setVisible(false)
        }

    }

    private fun setBarcode(barcode: StoreboxBarcode?) {
        if (barcode == null) {
            storeboxDetailBarcodeContainer.setVisible(false)
            return
        }

        try {
            val writer = MultiFormatWriter()
            val bm = writer.encode(barcode.value, BarcodeFormat.CODE_39, 400, 150)

            val width = bm.width
            val height = bm.height
            val pixels = IntArray(width * height)

            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bm.get(x, y)) BLACK else WHITE
                }
            }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

            if (bitmap != null) {
                storeboxDetailBarcodeContainer.setVisible(true)
                storeboxDetailIvBarcode.setImageBitmap(bitmap)
                storeboxDetailTvBarcode.text = barcode.displayValue
            } else {
                storeboxDetailBarcodeContainer.setVisible(false)
            }
        } catch (e: Exception) {
            storeboxDetailBarcodeContainer.setVisible(false)
            Timber.e(e)
        }
    }

    private fun showRemoveChannelDialog() {
        AlertDialog.Builder(context)
                .setTitle(Translation.storeboxreceipt.confirmDeleteTitle)
                .setMessage(Translation.storeboxreceipt.confirmDeleteMessage)
                .setPositiveButton(Translation.channelsettingsstoreboxadditions.deleteCardAlertButton.toUpperCase()) { dialog, which ->
                    presenter.deleteReceipt()
                }
                .setNegativeButton(Translation.channelsettingsstoreboxadditions.deleteCardCancelButton) { dialog, which ->

                }
                .create()
                .show()
    }

    override fun returnToMasterView() {
        fragmentManager.popBackStack()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Deal with return from document action sheet
        if (requestCode == OverlayActivity.REQUEST_ID) {
            when (data?.getSerializableExtra("res")) {
                (ButtonType.MOVE)   -> {
                    val i = Intent(context, FolderActivity::class.java)
                    i.putExtra("pick", true)
                    startActivityForResult(i, FolderActivity.REQUEST_ID)
                }
                (ButtonType.DELETE) -> {
                    showRemoveChannelDialog()
                }
                else                -> {
                    // Request do nothing

                }
            }
        }
        // deal with return from folder picker
        if (requestCode == FolderActivity.REQUEST_ID) {
            data?.extras?.let {
                val moveToFolder = data.getSerializableExtra("res")
                Timber.d("Move To Folder ${moveToFolder?.toString()}")
            }
        }
    }

    inner class PaymentLineAdapter : RecyclerView.Adapter<PaymentLineAdapter.PaymentLineViewHolder>() {
        var payments: ArrayList<StoreboxPayment> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentLineViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.viewholder_payment_line,
                    parent,
                    false
            )
            return PaymentLineViewHolder(view)
        }

        override fun getItemCount(): Int {
            return payments.size
        }

        override fun onBindViewHolder(holder: PaymentLineViewHolder, position: Int) {
            val payment = payments[position]
            holder.bind(payment)
        }

        inner class PaymentLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(payment: StoreboxPayment) {
                itemView.viewHolderPaymentTvCardName.text = payment.cardName
                itemView.viewHolderPaymentTvAmount.text = payment.priceValue
            }
        }
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
            val payment = receiptLines[position]
            holder.bind(payment)
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