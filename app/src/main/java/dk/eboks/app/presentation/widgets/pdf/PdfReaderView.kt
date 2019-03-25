package dk.eboks.app.presentation.widgets.pdf

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.eboks.app.R
import dk.eboks.app.util.inflate
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.viewholder_pdfpage.view.*
import timber.log.Timber

/**
 * Created by bison on 12-02-2018.
 */
class PdfReaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    AsyncPdfRenderer.PdfRendererListener,
    PageScrollListener.OnPageScrollChangeListener {

    private var pagesRecyclerView: RecyclerView
    private lateinit var adapter: PageAdapter
    private lateinit var renderer: AsyncPdfRenderer

    private var pages: List<RenderedPage> = listOf()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_pdfreader, this)
        pagesRecyclerView = findViewById(R.id.pagesRv)
        init()
    }

    private fun init() {
        adapter = PageAdapter()
        pagesRecyclerView.layoutManager = LinearLayoutManager(context)
        pagesRecyclerView.adapter = adapter
        pagesRecyclerView.addOnScrollListener(PageScrollListener(this))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Timber.e("onAttachedToWindow")
        renderer = AsyncPdfRenderer(context)
        renderer.listener = this
    }

    fun initWithFilename(filename: String) {
        Timber.d("show file $filename")
        renderer.start(filename)
        renderer.requestPage(0)
    }

    override fun onPageLoaded(bitmap: Bitmap, pageNumber: Int) {
        pages[pageNumber].page = bitmap
        Timber.d("Page: loaded: $pageNumber")
        adapter.notifyItemChanged(pageNumber)
    }

    override fun onPDFFileLoaded(pageCount: Int) {
        pages = List(pageCount) { RenderedPage() }
        adapter.notifyDataSetChanged()
        Timber.d("Pages: ${pages.size}")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Timber.e("onDetachedFromWindow")
        renderer.listener = null
        renderer.finish()
    }

    override fun onPageChanged(newPageNumber: Int) {
        Timber.d("onPageChanged: $newPageNumber")
        // Load currently shown page if not available
        if (!pages[newPageNumber].loaded) {
            renderer.requestPage(newPageNumber)
        }

        // Request next page if available
        val nextPage = newPageNumber + 1
        if (pages.size > nextPage && !pages[nextPage].loaded) {
            Timber.d("Loading page $nextPage")
            renderer.requestPage(nextPage)
        }
    }

    inner class PageAdapter : RecyclerView.Adapter<PageAdapter.PageViewHolder>() {

        inner class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindView(page: RenderedPage) {
                itemView.imageView.setImageBitmap(page.page)
                itemView.progressBar.isVisible = !page.loaded
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
            return PageViewHolder(parent.inflate(R.layout.viewholder_pdfpage))
        }

        override fun getItemCount() = pages.size

        override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
            val page = pages[position]
            holder.bindView(page)
        }
    }
}