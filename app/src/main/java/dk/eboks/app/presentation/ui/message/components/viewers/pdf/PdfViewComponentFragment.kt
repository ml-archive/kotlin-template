package dk.eboks.app.presentation.ui.message.components.viewers.pdf

import android.content.Context
import android.os.Bundle
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.ViewerFragment
import dk.eboks.app.mail.presentation.ui.message.components.viewers.base.EmbeddedViewer
import dk.eboks.app.mail.presentation.ui.message.components.viewers.pdf.PdfPrintAdapter
import dk.eboks.app.mail.presentation.ui.message.components.viewers.pdf.PdfViewComponentContract
import kotlinx.android.synthetic.main.fragment_pdfview_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class PdfViewComponentFragment : BaseFragment(), PdfViewComponentContract.View, EmbeddedViewer,
        ViewerFragment {

    @Inject
    lateinit var presenter: PdfViewComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pdfview_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun showPdfView(filename: String) {
        pdfView.initWithFilename(filename)
    }

    override fun updateView(folder: Folder) {
    }

    override fun print() {
        Timber.e("Print called")

        presenter.currentFile?.let {
            val printManager = context?.getSystemService(Context.PRINT_SERVICE) as PrintManager
            val adatpter = PdfPrintAdapter(it)
            printManager.print(it, adatpter, null)
        }
    }
}