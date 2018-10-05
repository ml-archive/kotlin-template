package dk.eboks.app.presentation.ui.mail.components.maillist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.folder.screens.FolderActivity
import dk.eboks.app.presentation.ui.mail.components.maillist.MailMessagesAdapter.MailMessageEvent.*
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.overlay.screens.ButtonType
import dk.eboks.app.presentation.ui.overlay.screens.OverlayActivity
import dk.eboks.app.presentation.ui.overlay.screens.OverlayButton
import dk.eboks.app.util.Starter
import dk.eboks.app.util.guard
import kotlinx.android.synthetic.main.fragment_mail_list_component.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.include_toolbar.*

class MailListComponentFragment : BaseFragment(), MailListComponentContract.View {
    @Inject
    lateinit var presenter: MailListComponentContract.Presenter

    private val adapter = MailMessagesAdapter()

    private var checkedList: MutableList<Message> = ArrayList()
    private var editEnabled: Boolean = false
    private var editAction: ButtonType? = null
    private var showUploads: Boolean = false

    var sender: Sender? = null

    var folder: Folder? = null
        set(value) {
            field = value
            adapter.folder = value
        }
    var modeEdit: Boolean = false
        set(value) {
            field = value
            adapter.editMode = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mail_list_component, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        showUploads = arguments?.getBoolean("showUploads", false) ?: false
        setupRecyclerView()
        setupFab()
        checkFabState()

        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }
        if (arguments == null) {
            onBackPressed()
            return
        }
        getFolderFromBundle()
        getSenderFromBundle()
        getEditFromBundle()
        setupTopBar()
    }

    override fun onResume() {
        super.onResume()
        if(refreshOnResume)
        {
            refreshOnResume = false
            presenter.refresh()
        }
        adapter?.let { it.notifyDataSetChanged() }
    }

    private fun getFolderFromBundle() {
        if (arguments.containsKey("folder")) {
            val folder = arguments.getSerializable("folder") as Folder
            this.folder = folder
            presenter.setup(folder)

        }
    }

    private fun getSenderFromBundle() {
        if (arguments.containsKey("sender")) {
            sender = arguments.getSerializable("sender") as Sender

            //this.folder = Folder(type = FolderType.LATEST, name = Translation.mail.allMail)
            sender?.let { presenter.setup(it) }
        }
    }


    private fun getEditFromBundle() {
        editEnabled = if (arguments.containsKey("edit")) {
            arguments.getSerializable("edit") as Boolean
        } else {
            true // enable edit mode as a default
        }
    }

    private fun setupFab() {
        mainFab.setOnClickListener {
            val i = Intent(context, OverlayActivity::class.java)
            i.putExtra("buttons", getActonButtons())
            startActivityForResult(i, OverlayActivity.REQUEST_ID)
        }
    }

    private fun getActonButtons(): ArrayList<OverlayButton> {
        val actionButtons = arrayListOf(
                OverlayButton(ButtonType.MOVE),
                OverlayButton(ButtonType.ARCHIVE)

        )
        var showRead = false
        var showUnread = false
        for (msg in checkedList){
            if (msg.unread == true ){ showRead = true}
            if (msg.unread == false ){ showUnread = true}
            if (showRead && showUnread) break
        }
        if (showRead) actionButtons.add(OverlayButton(ButtonType.READ))
        if (showUnread) actionButtons.add(OverlayButton(ButtonType.UNREAD))

        actionButtons.add(OverlayButton(ButtonType.DELETE))
        return actionButtons
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Deal with return from document action sheet
        if (requestCode == OverlayActivity.REQUEST_ID) {
            when (data?.getSerializableExtra("res")) {
                (ButtonType.MOVE)   -> {
                    editAction = ButtonType.MOVE
                    startFolderSelectActivity()
                }
                (ButtonType.DELETE) -> {
                    editAction = ButtonType.DELETE
                    presenter.deleteMessages(checkedList)
                    toggleEditMode()
                }
                (ButtonType.ARCHIVE)  -> {
                    editAction = ButtonType.ARCHIVE
                    presenter.archiveMessages(checkedList)
                    toggleEditMode()
                }
                (ButtonType.READ)  -> {
                    editAction = ButtonType.READ
                    presenter.markReadMessages(checkedList, false)
                    toggleEditMode()
                }
                (ButtonType.UNREAD)  -> {
                    editAction = ButtonType.UNREAD
                    presenter.markReadMessages(checkedList, true)
                    toggleEditMode()
                }
                else                -> {
                    // Request do nothing
                    editAction = null
                    toggleEditMode()
                }
            }
        }

        // deal with return from folder picker
        if (requestCode == FolderActivity.REQUEST_ID) {
            data?.extras?.let {
                val moveToFolder = data.getSerializableExtra("res") as Folder
                presenter.moveMessages(moveToFolder.id, checkedList)
                checkedList.clear()
                if (modeEdit) {
                    toggleEditMode()
                }
            }
        }
    }

    private fun setupTopBar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.mainTb?.navigationIcon = null
            onBackPressed()
        }

        if (editEnabled && BuildConfig.ENABLE_DOCUMENT_ACTIONS) {
            val menuProfile = getBaseActivity()?.mainTb?.menu?.add(Translation.uploads.topbarEdit)
            menuProfile?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            menuProfile?.setOnMenuItemClickListener { item: MenuItem ->
                toggleEditMode()
                true
            }
        }
        setTopBar()
    }

    private fun onBackPressed() {
        fragmentManager.popBackStack()
    }

    private fun toggleEditMode() {
        modeEdit = !modeEdit
        refreshSrl?.isEnabled = !modeEdit
        checkedList.clear()
        setTopBar()
        checkFabState()
        messagesRv.adapter.notifyItemRangeChanged(0, adapter.messages.size)
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
                    else               -> {
                        activity.mainTb.title = it.name
                    }
                }
            }.guard {
                sender?.let {
                    activity.mainTb.title = it.name
                }
            }

        }
    }

    private fun setupRecyclerView() {
        adapter.showUploads = showUploads
        messagesRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        messagesRv.adapter = adapter

        adapter.onActionEvent = { message, mailMessageEvent ->
            Timber.d("onActionEvent: %s", mailMessageEvent)
            when (mailMessageEvent) {
                OPEN -> {
                    editAction = ButtonType.OPEN
                    message.unread = false
                    startMessageOpenActivity(message)
                }
                READ -> {
                    editAction = ButtonType.READ
                    message.unread = false
                    presenter.updateMessage(message)
                }
                MOVE -> {
                    editAction = ButtonType.MOVE
                    checkedList.clear()
                    checkedList.add(message)
                    startFolderSelectActivity()
                }
            }
        }

        adapter.onMessageCheckedChanged = { isSelected, message ->
            if (isSelected) {
                checkedList.add(message)
            } else {
                checkedList.remove(message)
            }

            checkFabState()
        }

        messagesRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
                layoutManager?.let {
                    if(layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount-1){
                        onScrolledToLastItem()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    private fun onScrolledToLastItem()
    {
        presenter.loadNextPage()
    }

    private fun startMessageOpenActivity(message: Message) {
         activity.Starter()
                .activity(MessageOpeningActivity::class.java)
                .putExtra(Message::class.java.simpleName, message)
                .start()
    }

    private fun startFolderSelectActivity() {
        val i = Intent(context, FolderActivity::class.java)
        i.putExtra("pick", true)
        startActivityForResult(i, FolderActivity.REQUEST_ID)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.mainTb?.menu?.clear()
        activity?.mainTb?.title = ""
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
        Timber.e("Showing messages")
        checkedList.clear()
        adapter.messages.clear()
        adapter.messages.addAll(messages)
        messagesRv.adapter.notifyDataSetChanged()
    }

    override fun appendMessages(messages: List<Message>) {
        Timber.e("Appending messages")
        adapter.messages.addAll(messages)
        messagesRv.adapter.notifyDataSetChanged()
    }

    companion object {
        var refreshOnResume: Boolean = false
    }
}