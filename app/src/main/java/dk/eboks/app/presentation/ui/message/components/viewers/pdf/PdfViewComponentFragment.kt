package dk.eboks.app.presentation.ui.message.components.viewers.pdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import dk.eboks.app.R
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.ViewerFragment
import dk.eboks.app.presentation.ui.message.components.viewers.base.EmbeddedViewer
import dk.eboks.app.presentation.widgets.pdf.AsyncPdfRenderer
import dk.eboks.app.presentation.widgets.pdf.RenderedPage
import kotlinx.android.synthetic.main.fragment_pdfview_component.*
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class PdfViewComponentFragment : BaseFragment(), PdfViewComponentContract.View, EmbeddedViewer, ViewerFragment {
    private var pages: MutableList<RenderedPage> = ArrayList()
    private lateinit var renderer: AsyncPdfRenderer

    @Inject
    lateinit var presenter : PdfViewComponentContract.Presenter

    var pageMarginPx = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_pdfview_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)


    }


    override fun showPdfView(filename: String) {
      pdfView.initWithFilename(filename)
    }



    fun setupRecyclerView()
    {
      /*  pagesRv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.RecyclerView.VERTICAL,
            false
        )
        pagesRv.adapter = PageAdapter()*/
    }

    override fun updateView(folder: Folder) {

    }


    inner class PageAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<PageAdapter.PageViewHolder>() {

        inner class PageViewHolder(val pageIv : ImageView) : androidx.recyclerview.widget.RecyclerView.ViewHolder(pageIv)
        {
            //val circleIv = root.findViewById<ImageView>(R.id.circleIv)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
            //val v = LayoutInflater.from(context).inflate(R.layout.viewholder_pdf_page, parent, false)
            val pageiv = ImageView(context)
            val vh = PageViewHolder(pageiv)
            return vh
        }

        override fun getItemCount(): Int {
            return pages.size
        }

        override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
            val params = androidx.recyclerview.widget.RecyclerView.LayoutParams(612, 792)
            params.setMargins(pageMarginPx, pageMarginPx, pageMarginPx, pageMarginPx)
            holder.pageIv.layoutParams = params
            holder.pageIv.background = resources.getDrawable(R.color.white)
        }
    }

    override fun print() {
        Timber.e("Print called")
    }
}