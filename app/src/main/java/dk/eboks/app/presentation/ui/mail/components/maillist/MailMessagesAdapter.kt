package dk.eboks.app.presentation.ui.mail.components.maillist

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.daimajia.swipe.SwipeLayout
import dk.eboks.app.App
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessageType
import dk.eboks.app.util.getWorkaroundUrl
import kotlinx.android.synthetic.main.viewholder_message_row.view.*
import timber.log.Timber

class MailMessagesAdapter : RecyclerView.Adapter<MailMessagesAdapter.MessageViewHolder>() {
    enum class MailMessageEvent { OPEN, READ, MOVE }

    private val formatter: EboksFormatter = App.instance().appComponent.eboksFormatter()
    var editMode: Boolean = false
    private val messages = mutableListOf<Message>()
    var folder: Folder? = null
    var showUploads: Boolean = false

    var onMessageCheckedChanged: ((Boolean, Message) -> Unit)? = null
    var onActionEvent: ((Message, MailMessageEvent) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.viewholder_message_row,
            parent,
            false
        )
        return MessageViewHolder(v)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val last = (position == messages.size)
        holder.bind(messages[position], last)
    }

    fun setData(messages: List<Message>) {
        val diffResult = DiffUtil.calculateDiff(MessageDiff(this.messages, messages))
        this.messages.clear()
        this.messages += messages
        diffResult.dispatchUpdatesTo(this)
    }

    fun addMessages(messages: List<Message>) {
        val newList = this.messages + messages
        setData(newList)
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val swipeLayout = itemView as SwipeLayout
        private val contentContainer = itemView.mainContainerView
        private val markAsReadContainer = itemView.containerMarkAsRead
        private val moveContainer = itemView.containerMove
        // Main View
        private val headerTv = contentContainer.headerTv
        private val subHeaderTv = contentContainer.subHeaderTv
        private val dateTv = contentContainer.dateTv
        private val dividerV = contentContainer.dividerV
        private val checkBox = contentContainer.checkboxIb
        private val uploadFl = contentContainer.uploadFl
        private val urgentTv = contentContainer.urgentTv
        private val clipIv = contentContainer.clipIv
        private val imageIv = contentContainer.imageIv
        private val markAsReadTv = markAsReadContainer.markAsReadTv

        init {
            if (BuildConfig.ENABLE_DOCUMENT_ACTIONS) {
                swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
                swipeLayout.addDrag(SwipeLayout.DragEdge.Left, markAsReadContainer)
                swipeLayout.addDrag(SwipeLayout.DragEdge.Right, moveContainer)
            }
        }

        fun bind(currentItem: Message, last: Boolean) {
            Timber.e("binding msg viewholder: Sub ${currentItem.subject} Sender ${currentItem.sender?.name} unread=${currentItem.unread}")
            setGeneric(currentItem)

            if (BuildConfig.ENABLE_DOCUMENT_ACTIONS) {
                swipeLayout.isLeftSwipeEnabled = !editMode
                swipeLayout.isRightSwipeEnabled = !editMode
                if (!currentItem.unread) {
                    markAsReadTv.text = Translation.inbox.actionMarkAsUnread
                } else {
                    markAsReadTv.text = Translation.inbox.actionMarkAsRead
                }
            } else {
                swipeLayout.isLeftSwipeEnabled = false
                swipeLayout.isRightSwipeEnabled = false
            }

            if (editMode) {
                setSelectable(currentItem, last)
            } else {
                setMessage(currentItem)
            }

            markAsReadContainer.setOnClickListener {
                onActionEvent?.invoke(currentItem, MailMessageEvent.READ)
            }

            moveContainer.setOnClickListener {
                onActionEvent?.invoke(currentItem, MailMessageEvent.MOVE)
            }
        }

        private fun setGeneric(currentItem: Message) {
            if (currentItem.unread && currentItem.type != MessageType.UPLOAD) {
                headerTv.setTypeface(null, Typeface.BOLD)
                dateTv?.setTypeface(null, Typeface.BOLD)
                subHeaderTv?.setTypeface(null, Typeface.BOLD)
                dateTv?.setTextColor(ContextCompat.getColor(itemView.context, R.color.darkGreyBlue))
            } else {
                headerTv?.setTypeface(null, Typeface.NORMAL)
                dateTv?.setTypeface(null, Typeface.NORMAL)
                subHeaderTv.setTypeface(null, Typeface.NORMAL)
                dateTv?.setTextColor(ContextCompat.getColor(itemView.context, R.color.silver))
            }

            if (showUploads) {
                headerTv.text = currentItem.subject
                subHeaderTv.text = currentItem.folder?.name ?: ""
            } else {
                headerTv.text = currentItem.sender?.name
                subHeaderTv.text = currentItem.subject
            }
            dateTv.text = formatter.formatDateRelative(currentItem)
            checkBox.isSelected = false

            if (currentItem.status?.title != null) {
                urgentTv?.visibility = View.VISIBLE
                urgentTv?.text = currentItem.status?.title
                if (currentItem.status?.important == true) urgentTv.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.rougeTwo
                    )
                )
                else urgentTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.silver))
            } else {
                urgentTv?.visibility = View.GONE
            }

            if (currentItem.numberOfAttachments > 0) {
                clipIv?.visibility = View.VISIBLE
            } else {
                clipIv?.visibility = View.GONE
            }
        }

        private fun setMessage(currentItem: Message) {

            uploadFl.visibility = View.VISIBLE
            checkBox.visibility = View.GONE

            if (currentItem.type == MessageType.UPLOAD) {
                imageIv.setImageResource(R.drawable.ic_menu_uploads)
                uploadFl.isSelected = false
            } else {
                currentItem.sender?.logo?.let { logo ->
                    // Timber.e("Loading the logo at URL ${logo.getWorkaroundUrl()}")
                    Glide.with(itemView.context)
                        .applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_sender_placeholder))
                        .load(logo.getWorkaroundUrl())
                        .into(imageIv)

                    uploadFl.isSelected = currentItem.unread
                }
            }

            val messageListener = View.OnClickListener {
                onActionEvent?.invoke(currentItem, MailMessageEvent.OPEN)
            }

            contentContainer.setOnClickListener(messageListener)
            checkBox.setOnClickListener(messageListener)
        }

        private fun setSelectable(currentItem: Message, last: Boolean) {

            if (last) {
                dividerV.visibility = View.GONE
            }

            uploadFl.visibility = View.GONE
            checkBox.visibility = View.VISIBLE

            val uploadListener = View.OnClickListener {
                if (checkBox.visibility == View.VISIBLE) {

                    val invertedValue = !checkBox.isSelected
                    onMessageCheckedChanged?.invoke(invertedValue, currentItem)
                    checkBox.isSelected = invertedValue

                    if (uploadFl.visibility == View.VISIBLE) {
                        onActionEvent?.invoke(currentItem, MailMessageEvent.OPEN)
                    }
                }
            }

            contentContainer.setOnClickListener(uploadListener)
            checkBox.setOnClickListener(uploadListener)
        }
    }

    private inner class MessageDiff(val oldList: List<Message>, val newList: List<Message>) :
        DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}