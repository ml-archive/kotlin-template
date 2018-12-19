package dk.eboks.app.presentation.ui.uploads.screens.fileupload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseSheetActivity
import dk.eboks.app.presentation.ui.folder.screens.FolderActivity
import dk.eboks.app.presentation.ui.message.components.viewers.html.HtmlViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.image.ImageViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.pdf.PdfViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.text.TextViewComponentFragment
import dk.eboks.app.util.guard
import dk.eboks.app.util.putArg
import dk.eboks.app.util.setVisible
import dk.nodes.filepicker.uriHelper.FilePickerUriHelper
import kotlinx.android.synthetic.main.activity_base_sheet.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.sheet_file_upload.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FileUploadActivity : BaseSheetActivity(), FileUploadContract.View {
    @Inject
    lateinit var presenter: FileUploadContract.Presenter

    @Inject
    lateinit var formatter: EboksFormatter

    var embeddedViewerComponentFragment: androidx.fragment.app.Fragment? = null

    var destinationFolder : Folder? = null
    var uriString : String? = null
    var mimeType : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentSheet(R.layout.sheet_file_upload)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        mainTb.setVisible(false)

        mainHandler.post {
            intent.getStringExtra("uriString")?.let { uriString->
                mimeType = intent.getStringExtra("mimeType")
                presenter.setup(uriString, mimeType)
            }
        }

        if(presenter.isVerified())
        {
            chooseFolderLl.setOnClickListener {
                val i = Intent(this, FolderActivity::class.java)
                i.putExtra("pick", true)
                i.putExtra("selectFolder", true)
                startActivityForResult(i, FolderActivity.REQUEST_ID)
            }
        }

        cancelBtn.setOnClickListener {
            onBackPressed()
        }

        fileNameEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                validate()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        saveBtn.setOnClickListener {
            destinationFolder?.let {
                var intent = Intent()
                intent.putExtra("filename", fileNameEt.text.toString().trim())
                intent.putExtra("destinationFolderId", it.id)
                uriString.let { intent.putExtra("uriString", it) }
                mimeType.let { intent.putExtra("mimeType", it) }.guard { intent.putExtra("mimeType", "application/octet-stream") }
                setResult(Activity.RESULT_OK, intent)
                finishAfterTransition()
            }
        }

        if(!presenter.isVerified())
        {
            fileNameEt.isEnabled = false
            saveBtn.isEnabled = false
        }
        // set default result
        setResult(Activity.RESULT_CANCELED)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            destinationFolder = data?.getParcelableExtra("res")
            destinationTv.text = destinationFolder?.name ?: ""
        }
    }

    override fun setHighPeakHeight() {
        setupPeakHeight(140)
    }


    private fun validate()
    {
        saveBtn.isEnabled = destinationFolder != null && fileNameEt.text.toString().isNotEmpty()
    }

    override fun showFilename(uriString: String) {
        this.uriString = uriString
        val file = FilePickerUriHelper.getFile(this, uriString)
        try {
            val uri = Uri.parse(uriString)
            fileNameEt.setText(uri.lastPathSegment)
        }
        catch (t : Throwable)
        {
            fileNameEt.setText("")
        }
        validate()
    }

    override fun showDestinationFolder(folder: Folder) {
        destinationFolder = folder
        Timber.e("showDestinationFolder $folder")
        destinationTv.text = destinationFolder?.name ?: ""
        validate()
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

    companion object {
        val REQUEST_ID: Int = 2169
    }
}