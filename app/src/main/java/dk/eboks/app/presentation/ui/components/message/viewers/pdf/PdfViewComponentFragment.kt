package dk.eboks.app.presentation.ui.components.message.viewers.pdf

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Folder
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.pdf.AsyncPdfRenderer
import dk.eboks.app.util.pdf.RenderedPage
import kotlinx.android.synthetic.main.fragment_pdfview_component.*
import java.util.ArrayList
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Created by bison on 09-02-2018.
 */
class PdfViewComponentFragment : BaseFragment(), PdfViewComponentContract.View {
    private var pages: MutableList<RenderedPage> = ArrayList()
    private lateinit var renderer: AsyncPdfRenderer

    @Inject
    lateinit var presenter : PdfViewComponentContract.Presenter

    var pageMarginPx = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_pdfview_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        pageMarginPx = (resources.displayMetrics.density * 16f).roundToInt()

        pages.add(RenderedPage())
        pages.add(RenderedPage())
        pages.add(RenderedPage())
        setupRecyclerView()

        renderer = AsyncPdfRenderer(context)
        renderer.start("pdf.pdf")
        renderer.requestPage(0)
    }

    override fun setupTranslations() {

    }

    fun setupRecyclerView()
    {
        pagesRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        pagesRv.adapter = PageAdapter()
    }

    override fun updateView(folder: Folder) {

    }


    inner class PageAdapter : RecyclerView.Adapter<PageAdapter.PageViewHolder>() {

        inner class PageViewHolder(val pageIv : ImageView) : RecyclerView.ViewHolder(pageIv)
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

        override fun onBindViewHolder(holder: PageViewHolder?, position: Int) {
            val params = RecyclerView.LayoutParams(612, 792)
            params.setMargins(pageMarginPx, pageMarginPx, pageMarginPx, pageMarginPx)
            holder?.pageIv?.layoutParams = params
            holder?.pageIv?.background = resources.getDrawable(R.color.white)
        }
    }


}