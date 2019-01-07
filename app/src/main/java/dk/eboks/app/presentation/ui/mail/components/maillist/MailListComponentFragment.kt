package dk.eboks.app.presentation.ui.mail.components.maillist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mail_list_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        if (refreshOnResume) {
            refreshOnResume = false
            presenter.refresh()
        }
        adapter.let { it.notifyDataSetChanged() }
    }

    private fun getFolderFromBundle() {
        arguments?.getParcelable<Folder>("folder")?.let { folder ->
            this.folder = folder
            presenter.setup(folder)
        }
    }

    private fun getSenderFromBundle() {
        arguments?.getParcelable<Sender>("sender")?.let {
            sender = it
            presenter.setup(it)
        }
        //this.folder = Folder(type = FolderType.LATEST, name = Translation.mail.allMail)
    }

    private fun getEditFromBundle() {
        editEnabled = arguments?.getBoolean("edit", true) ?: true
    }

    private fun setupFab() {
        mainFab.setOnClickListener {
            startActivityForResult(OverlayActivity.createIntent(it.context, getActonButtons()), OverlayActivity.REQUEST_ID)
        }
    }

    private fun getActonButtons(): ArrayList<OverlayButton> {
        val actionButtons = arrayListOf(
            OverlayButton(ButtonType.MOVE),
            OverlayButton(ButtonType.ARCHIVE)

        )
        var showRead = false
        var showUnread = false
        for (msg in checkedList) {
            if (msg.unread) {
                showRead = true
            }
            if (!msg.unread) {
                showUnread = true
            }
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
                (ButtonType.MOVE) -> {
                    editAction = ButtonType.MOVE
                    startFolderSelectActivity()
                }
                (ButtonType.DELETE) -> {
                    editAction = ButtonType.DELETE
                    presenter.deleteMessages(checkedList)
                    toggleEditMode()
                }
                (ButtonType.ARCHIVE) -> {
                    editAction = ButtonType.ARCHIVE
                    presenter.archiveMessages(checkedList)
                    toggleEditMode()
                }
                (ButtonType.READ) -> {
                    editAction = ButtonType.READ
                    presenter.markReadMessages(checkedList, false)
                    toggleEditMode()
                }
                (ButtonType.UNREAD) -> {
                    editAction = ButtonType.UNREAD
                    presenter.markReadMessages(checkedList, true)
                    toggleEditMode()
                }
                else -> {
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
        fragmentManager?.popBackStack()
    }

    private fun toggleEditMode() {
        modeEdit = !modeEdit
        refreshSrl?.isEnabled = !modeEdit
        checkedList.clear()
        setTopBar()
        checkFabState()
        messagesRv.adapter?.notifyItemRangeChanged(0, adapter.messages.size)
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
        activity?.run {
            if (checkedList.size > 0) {
                mainTb.title = checkedList.size.toString() + " " + Translation.uploads.chosen
            } else {
                folder?.let {
                    when (it.type) {
                        FolderType.UPLOADS -> {
                            mainTb.title = Translation.uploads.title
                        }
                        else -> {
                            mainTb.title = it.name
                        }
                    }
                }.guard {
                    sender?.let {
                        mainTb.title = it.name
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter.showUploads = showUploads
        messagesRv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.RecyclerView.VERTICAL,
            false
        )
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
                    message.unread = !message.unread
                    presenter.markReadMessages(arrayListOf(message), message.unread)
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

        messagesRv.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
                layoutManager.let {
                    if (layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1) {
                        onScrolledToLastItem()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun onScrolledToLastItem() {
        presenter.loadNextPage()
    }

    private fun startMessageOpenActivity(message: Message) {
        activity?.run {
            Starter()
                .activity(MessageOpeningActivity::class.java)
                .putExtra(Message::class.java.simpleName, message)
                .start()
        }
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
        messagesRv.adapter?.notifyDataSetChanged()
    }

    override fun appendMessages(messages: List<Message>) {
        Timber.e("Appending messages")
        adapter.messages.addAll(messages)
        messagesRv.adapter?.notifyDataSetChanged()
    }

    companion object {
        var refreshOnResume: Boolean = false
    }
}