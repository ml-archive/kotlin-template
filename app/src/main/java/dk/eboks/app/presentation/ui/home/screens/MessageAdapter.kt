package dk.eboks.app.presentation.ui.home.screens

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.util.getWorkaroundUrl
import dk.eboks.app.util.inflate
import kotlinx.android.synthetic.main.viewholder_home_message.view.*
import java.lang.ref.WeakReference

class MessageAdapter(
    private val formatter: EboksFormatter,
    callback: MessageAdapter.Callback? = null
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    interface Callback {
        fun onMessageClick(message: Message)
    }

    private val list = mutableListOf<Message>()
    private val callbackWeakReference = WeakReference(callback)

    fun setData(list: List<Message>) {
        val diff = DiffUtil.calculateDiff(MessageDiff(this.list, list))
        this.list.clear()
        this.list += list
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.viewholder_home_message))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) {
            itemView.run {
                message.sender?.logo?.let {
                    Glide.with(context).load(it.url).into(circleIv)
                }

                if (message.unread) {
                    circleIv.isSelected = true
                    dateTv.setTypeface(null, Typeface.BOLD)
                    titleTv.setTypeface(null, Typeface.BOLD)
                } else {
                    dateTv.setTypeface(null, Typeface.NORMAL)
                    titleTv.setTypeface(null, Typeface.NORMAL)
                }

                message.sender?.logo?.let { logo ->
                    Glide.with(context ?: return)
                        .applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_sender_placeholder))
                        .load(logo.getWorkaroundUrl())
                        .into(circleIv)
                }

                if (message.status?.title != null) {
                    urgentTv?.visibility = View.VISIBLE
                    urgentTv?.text = message.status?.title
                    if (message.status?.important == true) {
                        urgentTv.setTextColor(ContextCompat.getColor(context, R.color.rougeTwo))
                    } else
                        urgentTv.setTextColor(ContextCompat.getColor(context, R.color.silver))
                } else {
                    urgentTv?.visibility = View.GONE
                }

                titleTv.text = message.sender?.name ?: ""
                subTitleTv.text = message.subject
                dateTv.text = formatter.formatDateRelative(message)
                if (adapterPosition == itemCount - 1) {
                    dividerV.visibility = View.GONE
                }

                rootLl?.setOnClickListener {
                    callbackWeakReference.get()?.onMessageClick(message)
                }
            }
        }
    }

    private class MessageDiff(
        private val oldList: List<Message>,
        private val newList: List<Message>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition].id == oldList[oldItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition] == oldList[oldItemPosition]
        }
    }
}