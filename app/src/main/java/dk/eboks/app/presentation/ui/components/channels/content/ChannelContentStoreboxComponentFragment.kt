package dk.eboks.app.presentation.ui.components.channels.content

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_channel_storebox_component.*
import kotlinx.android.synthetic.main.viewholder_channel_storebox_row.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelContentStoreboxComponentFragment : BaseFragment(), ChannelContentStoreboxComponentContract.View {

    var receipts: MutableList<String> = ArrayList()

    @Inject
    lateinit var presenter: ChannelContentStoreboxComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_storebox_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        receipts.add("blag")
        receipts.add("blag2")
        receipts.add("blag3")
        receipts.add("blag4")

        receiptRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        receiptRv.adapter = StoreboxAdapter()
    }


    inner class StoreboxAdapter : RecyclerView.Adapter<StoreboxAdapter.StoreboxViewHolder>() {

        inner class StoreboxViewHolder(val root: View) : RecyclerView.ViewHolder(root) {

            //cards
            val headerTv = root.findViewById<TextView>(R.id.headerTv)
            val subHeaderTv = root.findViewById<TextView>(R.id.subHeaderTv)
            val amountTv = root.findViewById<TextView>(R.id.amountTv)
            val dateTv = root.findViewById<TextView>(R.id.dateTv)
            val logoIv = root.findViewById<ImageView>(R.id.logoIv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreboxViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.viewholder_channel_storebox_row, parent, false)
            val vh = StoreboxViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return receipts.size
        }

        override fun onBindViewHolder(holder: StoreboxViewHolder?, position: Int) {
            var currentReceipt = receipts[position]

            holder?.headerTv?.text = "_header" + position
            holder?.subHeaderTv?.text = "_subheader" + position
            holder?.amountTv?.text = "_amount" + position
            holder?.dateTv?.text = "_date" + position

            if (currentReceipt != null) {
                holder?.logoIv?.let {
                    //                        Glide.with(context).load(currentCard?.logo?.url).into(it)
                }
            }

//                // on click
//                if (currentCard.installed == true) {
//                    holder?.button?.setText(Translation.channels.open)
//                    holder?.button?.setOnClickListener { presenter.open(currentCard) }
//
//                } else {
//                    holder?.button?.setText(Translation.channels.install)
//                    holder?.button?.setOnClickListener { presenter.install(currentCard) }
//                }

        }

    }
}