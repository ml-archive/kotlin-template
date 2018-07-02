package dk.eboks.app.presentation.ui.screens.message

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.message.detail.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.document.DocumentComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.notes.NotesComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.reply.ReplyButtonComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.share.ShareComponentFragment
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

class MessageActivity : BaseActivity(), MessageContract.View {
    @Inject lateinit var presenter: MessageContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    var headerComponentFragment: HeaderComponentFragment? = null
    var replyButtonComponentFragment: ReplyButtonComponentFragment? = null
    var documentComponentFragment: DocumentComponentFragment? = null
    var shareComponentFragment: ShareComponentFragment? = null
    var notesComponentFragment: NotesComponentFragment? = null
    var attachmentsComponentFragment: AttachmentsComponentFragment? = null
    var folderInfoComponentFragment: FolderInfoComponentFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setContentView(R.layout.activity_message)
        setupTopBar()
        presenter.setup()
    }

    private fun setupTopBar()
    {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = Translation.message.title
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun showTitle(message: Message) {
        mainTb.subtitle = formatter.formatDate(message)
    }

    override fun getNavigationMenuAction(): Int {
        return R.id.actionMail
    }

    override fun addHeaderComponentFragment()
    {
        headerComponentFragment = HeaderComponentFragment()
        headerComponentFragment?.let{
            it.arguments = Bundle()
            it.arguments.putBoolean("show_divider", false)
            supportFragmentManager.beginTransaction().add(R.id.headerContainerLl, it, HeaderComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addDocumentComponentFragment()
    {
        documentComponentFragment = DocumentComponentFragment()
        documentComponentFragment?.let{
            it.arguments = Bundle()
            it.arguments.putBoolean("show_divider", true)
            supportFragmentManager.beginTransaction().add(R.id.headerContainerLl, it, DocumentComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addReplyButtonComponentFragment(message: Message)
    {
        replyButtonComponentFragment = ReplyButtonComponentFragment()
        val args = Bundle()
        args.putSerializable(Message::class.java.simpleName, message)
        replyButtonComponentFragment?.let{
            it.arguments = args
            supportFragmentManager.beginTransaction().add(R.id.bodyContainerLl, it, ReplyButtonComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addShareComponentFragment() {
        shareComponentFragment = ShareComponentFragment()
        shareComponentFragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.bodyContainerLl, it, ShareComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addNotesComponentFragment() {
        notesComponentFragment = NotesComponentFragment()
        notesComponentFragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.bodyContainerLl, it, NotesComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addAttachmentsComponentFragment() {
        attachmentsComponentFragment = AttachmentsComponentFragment()
        attachmentsComponentFragment?.let {
            supportFragmentManager.beginTransaction().add(R.id.bodyContainerLl, it, AttachmentsComponentFragment::class.java.simpleName).commit()
        }
    }


    override fun addFolderInfoComponentFragment() {
        folderInfoComponentFragment = FolderInfoComponentFragment()
        folderInfoComponentFragment?.let {
            supportFragmentManager.beginTransaction().add(R.id.footerContainerLl, it, FolderInfoComponentFragment::class.java.simpleName).commit()
        }
    }
}
