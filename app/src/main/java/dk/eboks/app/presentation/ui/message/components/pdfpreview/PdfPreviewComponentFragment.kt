package dk.eboks.app.presentation.ui.message.components.pdfpreview

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Folder
import dk.eboks.app.presentation.ui.message.components.SheetComponentFragment
import javax.inject.Inject
import android.graphics.Bitmap
import android.os.ParcelFileDescriptor
import kotlinx.android.synthetic.main.fragment_pdfpreview_component.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.widget.Toast




/**
 * Created by bison on 09-02-2018.
 */
class PdfPreviewComponentFragment : SheetComponentFragment(), PdfPreviewComponentContract.View {

    @Inject
    lateinit var presenter : PdfPreviewComponentContract.Presenter
    private var pdfRenderer: PdfRenderer? = null
    private var fileDescriptor: ParcelFileDescriptor? = null
    private var currentPage: PdfRenderer.Page? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_pdfpreview_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)


        try {
            openRenderer(activity, "pdf.pdf")
            showPage(0)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(activity, "Error! " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        try {
            closeRenderer()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        super.onDestroyView()
    }

    override fun setupTranslations() {

    }

    override fun updateView(folder: Folder) {

    }


    /**
     * Sets up a [android.graphics.pdf.PdfRenderer] and related resources.
     */
    @Throws(IOException::class)
    private fun openRenderer(context: Context, FILENAME : String) {
        // In this sample, we read a PDF from the assets directory.
        val file = File(context.getCacheDir(), FILENAME)
        if (!file.exists()) {
            // Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
            // the cache directory.
            val asset = context.getAssets().open(FILENAME)
            val output = FileOutputStream(file)
            val buffer = ByteArray(8192)
            var size: Int
            while (true) {
                size = asset.read(buffer)
                if(size == -1)
                    break
                output.write(buffer, 0, size)
            }
            asset.close()
            output.close()
        }
        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        // This is the PdfRenderer we use to render the PDF.
        if (fileDescriptor != null) {
            pdfRenderer = PdfRenderer(fileDescriptor)
        }
    }

    /**
     * Closes the [android.graphics.pdf.PdfRenderer] and related resources.
     *
     * @throws java.io.IOException When the PDF file cannot be closed.
     */
    @Throws(IOException::class)
    private fun closeRenderer() {
        currentPage?.close()
        pdfRenderer?.close()
        fileDescriptor?.close()
    }

    /**
     * Shows the specified page of PDF to the screen.
     *
     * @param index The page index.
     */
    private fun showPage(index: Int) {
        if(pdfRenderer == null)
            return
        if (pdfRenderer!!.getPageCount() <= index) {
            return
        }
        // Make sure to close the current page before opening another one.
        if (null != currentPage) {
            currentPage!!.close()
        }
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer!!.openPage(index)
        // Important: the destination bitmap must be ARGB (not RGB).
        val bitmap = Bitmap.createBitmap(currentPage!!.width, currentPage!!.height, Bitmap.Config.ARGB_8888)
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        // We are ready to show the Bitmap to user.
        pageIv.setImageBitmap(bitmap)
    }
}