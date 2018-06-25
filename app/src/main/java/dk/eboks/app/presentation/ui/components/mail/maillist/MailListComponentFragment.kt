package dk.eboks.app.presentation.ui.components.mail.maillist

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
import dk.eboks.app.presentation.ui.components.mail.maillist.MailMessagesAdapter.MailMessageEvent.*
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderActivity
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.screens.overlay.ButtonType
import dk.eboks.app.presentation.ui.screens.overlay.OverlayActivity
import dk.eboks.app.presentation.ui.screens.overlay.OverlayButton
import dk.eboks.app.util.Starter
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

    private var actionButtons = arrayListOf(
            OverlayButton(ButtonType.MOVE),
            OverlayButton(ButtonType.ARCHIVE),
            OverlayButton(ButtonType.READ),
            OverlayButton(ButtonType.UNREAD),
            OverlayButton(ButtonType.DELETE)
    )

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

    private fun getFolderFromBundle() {
        if (arguments.containsKey("folder")) {
            val folder = arguments.getSerializable("folder") as Folder
            this.folder = folder
            presenter.setup(folder)

        }
    }

    private fun getSenderFromBundle() {
        if (arguments.containsKey("sender")) {
            val sender = arguments.getSerializable("sender") as Sender
            // todo correct folder type ?
            this.folder = Folder(type = FolderType.LATEST, name = Translation.mail.allMail)
            presenter.setup(sender)
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
            i.putExtra("buttons", actionButtons)
            startActivityForResult(i, OverlayActivity.REQUEST_ID)
        }
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
                    presenter.markReadMessages(checkedList)
                    toggleEditMode()
                }
                (ButtonType.UNREAD)  -> {
                    editAction = ButtonType.UNREAD
                    presenter.markUnreadMessages(checkedList)
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
                val moveToFolder = data.getSerializableExtra("res")

                Timber.d("Move To Folder ${moveToFolder?.toString()}")

                presenter.moveMessages(moveToFolder?.toString(), checkedList)

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
            }

        }
    }

    private fun setupRecyclerView() {
        messagesRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        messagesRv.adapter = adapter

        adapter.onActionEvent = { message, mailMessageEvent ->
            Timber.d("onActionEvent: %s", mailMessageEvent)

            when (mailMessageEvent) {
                OPEN -> {
                    editAction = ButtonType.OPEN
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
        checkedList.clear()
        adapter.messages.clear()
        adapter.messages.addAll(messages)
        messagesRv.adapter.notifyDataSetChanged()
    }
}