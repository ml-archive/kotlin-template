package dk.eboks.app.presentation.ui.components.mail.maillist

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.screens.overlay.ButtonType
import dk.eboks.app.presentation.ui.screens.overlay.OverlayActivity
import dk.eboks.app.presentation.ui.screens.overlay.OverlayButton
import dk.eboks.app.util.Starter
import dk.eboks.app.util.guard
import kotlinx.android.synthetic.main.fragment_mail_list_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MailListComponentFragment : BaseFragment(), MailListComponentContract.View {

    @Inject
    lateinit var presenter: MailListComponentContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    var messages: MutableList<Message> = ArrayList()
    var checkedList: MutableList<Message> = ArrayList()
    var folder: Folder? = null
    var modeEdit: Boolean = false
    var editEnabled: Boolean = false


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_mail_list_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupRecyclerView()
        setupFab(createFabButtonMocks())
        checkFabState()

        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }

        arguments?.let { args ->
            if (args.containsKey("folder")) {
                val folder = args.getSerializable("folder") as Folder
                this.folder = folder
                presenter.setup(folder)

            }
            if (args.containsKey("sender")) {
                val sender = args.getSerializable("sender") as Sender
                //todo correct folder type ?
                this.folder = Folder(type = FolderType.LATEST,name = Translation.mail.allMail )
                presenter.setup(sender)
            }

            if (args.containsKey("edit")) {
                editEnabled = args.getSerializable("edit") as Boolean
            } else
                editEnabled = true // enable edit mode as a default
        }.guard{
            onBackPressed()
        }
        // cannot setup topbar before folder been initialized
        setupTopBar()
    }

    private fun createFabButtonMocks(): ArrayList<OverlayButton> {
        var buttons: ArrayList<OverlayButton> = ArrayList()
        buttons.add(OverlayButton(ButtonType.MOVE))
        buttons.add(OverlayButton(ButtonType.DELETE))
        buttons.add(OverlayButton(ButtonType.PRINT))
        buttons.add(OverlayButton(ButtonType.MAIL))
        buttons.add(OverlayButton(ButtonType.OPEN))
        return buttons
    }

    private fun setupFab(buttons: ArrayList<OverlayButton> = ArrayList()) {
        mainFab.setOnClickListener {
            var i = Intent(context, OverlayActivity::class.java)

            i.putExtra("buttons", buttons)
            startActivityForResult(i, 1)
        }
    }

    private fun setupTopBar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.mainTb?.setNavigationIcon(null)
            onBackPressed()
        }

        if (editEnabled) {
            val menuProfile = getBaseActivity()?.mainTb?.menu?.add(Translation.uploads.topbarEdit)
            menuProfile?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            menuProfile?.setOnMenuItemClickListener { item: MenuItem ->
                switchMode()
                true
            }
        }
        setTopBar()
    }

    private fun onBackPressed() {
        fragmentManager.popBackStack()
    }

    private fun switchMode() {
        modeEdit = !modeEdit
        refreshSrl.isEnabled = !modeEdit
        checkedList.clear()
        setTopBar()
        checkFabState()
        messagesRv.adapter.notifyItemRangeChanged(0, messages.size)
    }

    private fun checkFabState() {
        if (checkedList.size > 0) {
            mainFab.show()
        } else {
            mainFab.hide()
        }
        setTopBar()
    }

    private fun setTopBar() {
        if (checkedList.size > 0) {
            activity.mainTb.title = checkedList.size.toString() + " " + Translation.uploads.chosen
        } else {
            folder?.let {
                when (it.type) {
                    FolderType.UPLOADS -> {
                        activity.mainTb.title = Translation.uploads.title
                    }
                    else -> {
                        activity.mainTb.title = it.name
                    }
                }
            }

        }
    }

    fun setupRecyclerView() {
        messagesRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        messagesRv.adapter = MessageAdapter()
    }

    override fun showRefreshProgress(show: Boolean) {
        refreshSrl.isRefreshing = show
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showEmpty(show: Boolean) {
        emptyFl.visibility = if (show) View.VISIBLE else View.GONE
        contentFl.visibility = if (!show) View.VISIBLE else View.GONE
    }

    override fun showMessages(messages: List<Message>) {
        this.messages.clear()
        this.messages.addAll(messages)
        messagesRv.adapter.notifyDataSetChanged()
    }

    inner class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

        inner class MessageViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
            val headerTv = root.findViewById<TextView>(R.id.headerTv)
            val subHeaderTv = root.findViewById<TextView>(R.id.subHeaderTv)
            val dateTv = root.findViewById<TextView>(R.id.dateTv)
            val dividerV = root.findViewById<View>(R.id.dividerV)
            val checkBox = root.findViewById<ImageButton>(R.id.checkboxIb)
            val uploadFl = root.findViewById<FrameLayout>(R.id.uploadFl)
            val urgentTv = root.findViewById<TextView>(R.id.urgentTv)
            val clipIv = root.findViewById<ImageView>(R.id.clipIv)
            val imageIv = root.findViewById<ImageView>(R.id.imageIv)


            fun bind(currentItem: Message, last: Boolean) {

                setGeneric(currentItem)
                if (modeEdit) {
                    setSelectable(currentItem, last)
                } else {
                    setMessage(currentItem)
                }

            }

            private fun setGeneric(currentItem: Message) {
                if (currentItem.unread) {
                    headerTv.setTypeface(null, Typeface.BOLD)
                    dateTv?.setTypeface(null, Typeface.BOLD)
                    subHeaderTv?.setTypeface(null, Typeface.BOLD)
                    dateTv?.setTextColor(resources.getColor(R.color.darkGreyBlue))
                } else {
                    headerTv?.setTypeface(null, Typeface.NORMAL)
                    dateTv?.setTypeface(null, Typeface.NORMAL)
                    subHeaderTv.setTypeface(null, Typeface.NORMAL)
                }

                headerTv.text = currentItem.sender?.name
                subHeaderTv.text = currentItem.subject
                dateTv.text = formatter.formatDateRelative(currentItem)
                checkBox.isSelected = false


                if (currentItem.status?.text != null) {
                    urgentTv?.visibility = View.VISIBLE
                    urgentTv?.text = currentItem.status?.text
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


                folder?.let {
                    if (it.type == FolderType.UPLOADS) {
                        imageIv.setImageDrawable(resources.getDrawable(R.drawable.ic_menu_uploads))
                        uploadFl.isSelected = false

                    } else {
                        currentItem?.sender?.let {
                            imageIv?.let {
                                Glide.with(context)
                                        .applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.icon_48_profile_grey))
                                        .load(currentItem.sender?.logo?.url)
                                        .into(it)

                                uploadFl.isSelected = currentItem.unread
                            }
                        }
                    }
                }
                val messageListener = View.OnClickListener {
                    activity.Starter()
                            .activity(MessageOpeningActivity::class.java)
                            .putExtra(Message::class.java.simpleName, messages[position])
                            .start()
                }

                root.setOnClickListener(messageListener)
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
                        //adding or removing item to checked list
                        if (!checkBox.isSelected) {
                            checkedList.add(currentItem)
                        } else {
                            checkedList.remove(currentItem)
                        }

                        // UI
                        checkBox.isSelected = !checkBox.isSelected
                        checkFabState()

                        if (uploadFl.visibility == View.VISIBLE) {
                            activity.Starter()
                                    .activity(MessageOpeningActivity::class.java)
                                    .putExtra(Message::class.java.simpleName, currentItem)
                                    .start()
                        }
                    }
                }
                root.setOnClickListener(uploadListener)
                checkBox.setOnClickListener(uploadListener)
            }
        }

        fun updateViews() {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.viewholder_message_row, parent, false)
            val vh = MessageViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return messages.size
        }

        override fun onBindViewHolder(holder: MessageViewHolder?, position: Int) {
            var last = (position == messages.size)
            holder?.bind(messages[position], last)
        }


    }

}