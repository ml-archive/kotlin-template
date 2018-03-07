package dk.eboks.app.presentation.ui.screens.message.embedded

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseSheetActivity
import dk.eboks.app.presentation.ui.components.message.detail.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.notes.NotesComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.share.ShareComponentFragment
import dk.eboks.app.presentation.ui.components.message.viewers.html.HtmlViewComponentFragment
import dk.eboks.app.presentation.ui.components.message.viewers.image.ImageViewComponentFragment
import dk.eboks.app.presentation.ui.components.message.viewers.pdf.PdfViewComponentFragment
import dk.eboks.app.presentation.ui.components.message.viewers.text.TextViewComponentFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MessageEmbeddedActivity : BaseSheetActivity(), MessageEmbeddedContract.View {
    @Inject
    lateinit var presenter: MessageEmbeddedContract.Presenter

    @Inject
    lateinit var formatter: EboksFormatter

    var headerComponentFragment: HeaderComponentFragment? = null
    var shareComponentFragment: ShareComponentFragment? = null
    var notesComponentFragment: NotesComponentFragment? = null
    var attachmentsComponentFragment: AttachmentsComponentFragment? = null
    var folderInfoComponentFragment: FolderInfoComponentFragment? = null
    var embeddedViewerComponentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentSheet(R.layout.sheet_message)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun showError(msg: String) {
        Timber.e(msg)
    }

    override fun setupTranslations() {

    }

    override fun addHeaderComponentFragment()
    {
        headerComponentFragment = HeaderComponentFragment()
        headerComponentFragment?.let{
            it.arguments = Bundle()
            it.arguments.putBoolean("show_divider", true)
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, it, HeaderComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addShareComponentFragment() {
        shareComponentFragment = ShareComponentFragment()
        shareComponentFragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, it, ShareComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addNotesComponentFragment() {
        notesComponentFragment = NotesComponentFragment()
        notesComponentFragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, it, NotesComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addAttachmentsComponentFragment() {
        attachmentsComponentFragment = AttachmentsComponentFragment()
        attachmentsComponentFragment?.let {
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, it, AttachmentsComponentFragment::class.java.simpleName).commit()
        }
    }


    override fun addFolderInfoComponentFragment() {
        folderInfoComponentFragment = FolderInfoComponentFragment()
        folderInfoComponentFragment?.let {
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, it, FolderInfoComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addPdfViewer() {
        Handler(mainLooper).post({
            embeddedViewerComponentFragment = PdfViewComponentFragment()
            embeddedViewerComponentFragment?.let {
                supportFragmentManager.beginTransaction().add(R.id.viewerFl, it, PdfViewComponentFragment::class.java.simpleName).commit()
            }
        })
    }

    override fun addImageViewer() {
        Handler(mainLooper).post({
            embeddedViewerComponentFragment = ImageViewComponentFragment()
            embeddedViewerComponentFragment?.let {
                supportFragmentManager.beginTransaction().add(R.id.viewerFl, it, ImageViewComponentFragment::class.java.simpleName).commit()
            }
        })
    }

    override fun addHtmlViewer() {
        Handler(mainLooper).post({
            embeddedViewerComponentFragment = HtmlViewComponentFragment()
            embeddedViewerComponentFragment?.let {
                supportFragmentManager.beginTransaction().add(R.id.viewerFl, it, HtmlViewComponentFragment::class.java.simpleName).commit()
            }
        })
    }

    override fun addTextViewer() {
        Handler(mainLooper).post({
            embeddedViewerComponentFragment = TextViewComponentFragment()
            embeddedViewerComponentFragment?.let {
                supportFragmentManager.beginTransaction().add(R.id.viewerFl, it, TextViewComponentFragment::class.java.simpleName).commit()
            }
        })

    }

    override fun showTitle(message: Message) {
        setToolbar(R.drawable.ic_menu_mail, Translation.message.title, formatter.formatDate(message))
    }
}