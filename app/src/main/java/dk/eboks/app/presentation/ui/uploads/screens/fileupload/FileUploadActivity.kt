package dk.eboks.app.presentation.ui.uploads.screens.fileupload

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseSheetActivity
import dk.eboks.app.presentation.ui.message.components.viewers.html.HtmlViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.image.ImageViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.pdf.PdfViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.text.TextViewComponentFragment
import dk.eboks.app.util.putArg
import dk.eboks.app.util.setVisible
import kotlinx.android.synthetic.main.activity_base_sheet.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FileUploadActivity : BaseSheetActivity(), FileUploadContract.View {
    @Inject
    lateinit var presenter: FileUploadContract.Presenter

    @Inject
    lateinit var formatter: EboksFormatter

    var embeddedViewerComponentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentSheet(R.layout.sheet_file_upload)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        mainTb.setVisible(false)

        mainHandler.post {
            intent.getStringExtra("uriString")?.let { uriString->
                var mimeType : String? = intent.getStringExtra("mimeType")
                presenter.setup(uriString, mimeType)
            }
        }

    }

    override fun setHighPeakHeight() {
        setupPeakHeight(140)
    }


    override fun showFilename(filename: String) {

    }

    override fun showDestinationFolder(folder: Folder) {

    }

    override fun showNoPreviewAvailable() {
        noPreviewAvailableTv.setVisible(true)
    }

    override fun addPdfViewer(uri : String) {
        Handler(mainLooper).post({
            embeddedViewerComponentFragment = PdfViewComponentFragment()
            embeddedViewerComponentFragment?.putArg("URI", uri)
            embeddedViewerComponentFragment?.let {
                supportFragmentManager.beginTransaction().add(R.id.viewerFl, it, PdfViewComponentFragment::class.java.simpleName).commit()
            }
        })
    }

    override fun addImageViewer(uri : String) {
        Handler(mainLooper).post({
            embeddedViewerComponentFragment = ImageViewComponentFragment()
            embeddedViewerComponentFragment?.putArg("URI", uri)
            embeddedViewerComponentFragment?.let {
                supportFragmentManager.beginTransaction().add(R.id.viewerFl, it, ImageViewComponentFragment::class.java.simpleName).commit()
            }
        })
    }

    override fun addHtmlViewer(uri : String) {
        Handler(mainLooper).post({
            embeddedViewerComponentFragment = HtmlViewComponentFragment()
            embeddedViewerComponentFragment?.putArg("URI", uri)
            embeddedViewerComponentFragment?.let {
                supportFragmentManager.beginTransaction().add(R.id.viewerFl, it, HtmlViewComponentFragment::class.java.simpleName).commit()
            }
        })
    }

    override fun addTextViewer(uri : String) {
        Handler(mainLooper).post({
            embeddedViewerComponentFragment = TextViewComponentFragment()
            embeddedViewerComponentFragment?.putArg("URI", uri)
            embeddedViewerComponentFragment?.let {
                supportFragmentManager.beginTransaction().add(R.id.viewerFl, it, TextViewComponentFragment::class.java.simpleName).commit()
            }
        })

    }

    override fun shouldStartExpanded(): Boolean {
        return true
    }

    override fun getNavigationMenuAction(): Int { return R.id.actionMail }
}