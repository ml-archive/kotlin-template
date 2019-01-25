package dk.eboks.app.presentation.ui.senders.screens.overview

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.CollectionContainerTypeEnum
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.sender.typeEnum
import dk.eboks.app.util.inflate
import kotlinx.android.synthetic.main.fragment_segment_component.view.*
import kotlinx.android.synthetic.main.fragment_sender_component.view.*
import kotlinx.android.synthetic.main.viewholder_sender_item.view.*
import timber.log.Timber
import java.lang.ref.WeakReference

class SendersCollectionAdapter(callback: Callback? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback {
        fun onSenderClick(sender: Sender)
        fun onRegisterSenderClick(sender: Sender)
        fun onUnregisterSenderClick(sender: Sender)
        fun onSegmentClick(segment: Segment)
    }

    private val callbackWeakReference = WeakReference(callback)

    private val data = mutableListOf<CollectionContainer>()

    fun setData(collections: List<CollectionContainer>) {
        data.clear()
        data += collections
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CollectionContainerTypeEnum.SEGMENT.ordinal -> {
                SegmentViewHolder(parent.inflate(R.layout.fragment_segment_component))
            }
            CollectionContainerTypeEnum.SENDER.ordinal -> {
                SenderViewHolder(parent.inflate(R.layout.fragment_sender_component))
            }
            CollectionContainerTypeEnum.SENDERS.ordinal -> {
                SenderCollectionViewHolder(parent.inflate(R.layout.viewholder_sender_item))
            }
            else -> throw UnknownError("Unknown collection type")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Timber.v("onBindViewHolder type ${data[position].type}")
        when (holder) {
            is SegmentViewHolder -> holder.bind(data[position])
            is SenderViewHolder -> data[position].sender!!.let(holder::bind)
            is SenderCollectionViewHolder -> holder.bind(data[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].typeEnum.ordinal
    }

    private inner class SegmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(collectionContainer: CollectionContainer) {
            itemView.run {
                segmentTitleTv.text = collectionContainer.description ?: ""
                collectionContainer.segment?.let { seg ->
                    Glide.with(context ?: return)
                        .load(seg.image?.url)
                        .apply(
                            RequestOptions()
                                .fallback(R.drawable.icon_72_senders_private)
                                .placeholder(R.drawable.icon_72_senders_private)
                        )
                        .into(segmentIv)
                    segmentIv.clipToOutline = true

                    segmentCv.setOnClickListener {
                        callbackWeakReference.get()?.onSegmentClick(seg)
//                        val i = Intent(context, SegmentDetailActivity::class.java)
//                        i.putExtra(Segment::class.simpleName, seg)
//                        startActivity(i)
                    }
                    segmentCatTv.text = seg.name
                    segmentSignTv.text = when (seg.registered) {
                        0 -> Translation.senders.register
                        else -> Translation.senders.registered
                    }
                }
            }
        }
    }

    private inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(sender: Sender) {
            itemView.run {
                Glide.with(context ?: return)
                    .load(sender.logo?.url)
                    .apply(
                        RequestOptions()
                            .fallback(R.drawable.icon_64_senders_private)
                            .placeholder(R.drawable.icon_64_senders_private)
                    )
                    .into(senderIv)
                senderNameTv.text = sender.name
                senderCv.setOnClickListener {
                    callbackWeakReference.get()?.onSenderClick(sender)
//                    val i = Intent(context, SenderDetailActivity::class.java)
//                    i.putExtra(Sender::class.simpleName, sender)
//                    startActivity(i)
                }

                setButtonText(senderRegisterBtn, sender)
                senderRegisterBtn.setOnClickListener { v ->
                    if (sender.registered != 0) {
                        AlertDialog.Builder(v.context)
                            .setTitle(Translation.senders.unregisterAlertTitle)
                            .setMessage(Translation.senders.unregisterAlertDescription)
                            .setNegativeButton(Translation.defaultSection.cancel) { dialog, _ ->
                                dialog.cancel()
                            }
                            .setPositiveButton(Translation.defaultSection.ok) { dialog, _ ->
                                callbackWeakReference.get()?.onUnregisterSenderClick(sender)
//                                presenter.unregisterSender(sender)
                                sender.registered = 0
                                setButtonText(senderRegisterBtn, sender)
                                dialog.dismiss()
                            }
                            .show()
                    } else {
                        AlertDialog.Builder(v.context)
                            .setTitle(Translation.senders.registerAlertTitle)
                            .setMessage(Translation.senders.registerAlertDescription)
                            .setNegativeButton(Translation.defaultSection.cancel) { dialog, _ ->
                                dialog.cancel()
                            }
                            .setPositiveButton(Translation.defaultSection.ok) { dialog, _ ->
                                callbackWeakReference.get()?.onRegisterSenderClick(sender)
//                                presenter.registerSender(sender)
                                sender.registered = 1
                                setButtonText(senderRegisterBtn, sender)
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }
        }
    }

    private inner class SenderCollectionViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val sendersAdapter = SendersAdapter()

        fun bind(collectionContainer: CollectionContainer) {
            itemView.sendersTitleTv.text = collectionContainer.description
            sendersAdapter.setData(collectionContainer.senders ?: emptyList())
            itemView.recyclerView.adapter = sendersAdapter
        }
    }

    private inner class SendersAdapter : RecyclerView.Adapter<SendersAdapter.ViewHolder>() {

        private val data = mutableListOf<Sender>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(parent.inflate(R.layout.viewholder_sender))
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position])
        }

        fun setData(list: List<Sender>) {
            data.clear()
            data += list
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val senderNameTv = itemView.findViewById<TextView>(R.id.senderNameTv)!!
            val senderRegisterBtn = itemView.findViewById<Button>(R.id.senderRegisterBtn)!!
            val senderLogoIv = itemView.findViewById<ImageView>(R.id.senderLogoIv)!!

            fun bind(sender: Sender) {

                Glide.with(itemView.context ?: return)
                    .load(sender.logo?.url)
                    .apply(
                        RequestOptions()
                            .fallback(R.drawable.icon_64_senders_private)
                            .placeholder(R.drawable.icon_64_senders_private)
                    )
                    .into(senderLogoIv)
                senderNameTv.text = sender.name
                senderRegisterBtn.visibility = View.VISIBLE
                setButtonText(senderRegisterBtn, sender)

                senderRegisterBtn.setOnClickListener { v ->
                    if (sender.registered != 0) {
                        AlertDialog.Builder(v.context)
                            .setTitle(Translation.senders.unregisterAlertTitle)
                            .setMessage(Translation.senders.unregisterAlertDescription)
                            .setNegativeButton(Translation.defaultSection.cancel) { dialog, _ ->
                                dialog.cancel()
                            }
                            .setPositiveButton(Translation.defaultSection.ok) { dialog, _ ->
                                callbackWeakReference.get()?.onUnregisterSenderClick(sender)
//                                    presenter.unregisterSender(sender)
                                sender.registered = 0
                                setButtonText(senderRegisterBtn, sender)
                                dialog.dismiss()
                            }
                            .show()
                    } else {
                        AlertDialog.Builder(v.context)
                            .setTitle(Translation.senders.registerAlertTitle)
                            .setMessage(Translation.senders.registerAlertDescription)
                            .setNegativeButton(Translation.defaultSection.cancel) { dialog, _ ->
                                dialog.cancel()
                            }
                            .setPositiveButton(Translation.defaultSection.ok) { dialog, _ ->
                                callbackWeakReference.get()?.onRegisterSenderClick(sender)
//                                    presenter.registerSender(sender)
                                sender.registered = 1
                                setButtonText(senderRegisterBtn, sender)
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
                itemView.setOnClickListener {
                    callbackWeakReference.get()?.onSenderClick(sender)
//                        val i = Intent(context, SenderDetailActivity::class.java)
//                        i.putExtra(Sender::class.simpleName, sender)
//                        startActivity(i)
                }
            }
        }
    }

    private fun setButtonText(textView: TextView, sender: Sender) {
        textView.text = when (sender.registered) {
            0 -> Translation.senders.register
            else -> Translation.senders.registered
        }
    }
}
