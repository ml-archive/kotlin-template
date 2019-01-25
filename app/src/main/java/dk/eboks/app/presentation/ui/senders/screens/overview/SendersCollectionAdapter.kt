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
import dk.eboks.app.domain.models.SenderCategory
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
import kotlinx.android.synthetic.main.viewholder_title_subtitle.view.*
import timber.log.Timber
import java.lang.ref.WeakReference

class SendersCollectionAdapter(callback: Callback? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback {
        fun onSenderClick(sender: Sender)
        fun onRegisterSenderClick(sender: Sender)
        fun onUnregisterSenderClick(sender: Sender)
        fun onSegmentClick(segment: Segment)
        fun onCategoryClick(category: SenderCategory)
    }

    private val callbackWeakReference = WeakReference(callback)

    private val collections = mutableListOf<CollectionContainer>()
    private val categories = mutableListOf<SenderCategory>()
    fun setCollections(collections: List<CollectionContainer>) {
        this.collections.clear()
        this.collections += collections
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
            TITLE -> {
                TitleViewHolder(parent.inflate(R.layout.viewholder_title))
            }
            CATEGORY -> {
                CategoriesViewHolder(parent.inflate(R.layout.viewholder_title_subtitle))
            }
            else -> throw UnknownError("Unknown collection type")
        }
    }

    override fun getItemCount(): Int {
        return collections.size + categories.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SegmentViewHolder -> holder.bind(collections[position])
            is SenderViewHolder -> collections[position].sender!!.let(holder::bind)
            is SenderCollectionViewHolder -> holder.bind(collections[position])
            is CategoriesViewHolder -> holder.bind(categories[position - collections.size - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < collections.size -> collections[position].typeEnum.ordinal
            position == collections.size -> TITLE
            else -> CATEGORY
        }
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

    private inner class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: SenderCategory) {
            itemView.run {
                titleTv.text = category.name
                subTv.text = "${category.numberOfSenders}"
                setOnClickListener {
                    callbackWeakReference.get()?.onCategoryClick(category)
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

    private class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setCategories(categories: List<SenderCategory>) {
        this.categories.clear()
        this.categories += categories
        notifyDataSetChanged()
    }

    companion object {
        private const val TITLE = 4
        private const val CATEGORY = 5
    }
}
