package dk.eboks.app.presentation.widgets.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue

/**
 * Created by bison on 12-02-2018.
 */
class AsyncPdfRenderer(val context: Context) : Runnable {
    private val thread : Thread = Thread(this)
    private val requestQueue : BlockingQueue<RenderRequest> = LinkedBlockingQueue<RenderRequest>()
    private val pageCache : ConcurrentHashMap<Int, RenderedPage> = ConcurrentHashMap()

    private var pdfRenderer: PdfRenderer? = null
    private var fileDescriptor: ParcelFileDescriptor? = null
    //private var currentPage: PdfRenderer.Page? = null
    private var filename: String = ""

    override fun run() {
        Timber.e("Starting AsyncPdfRenderer thread, going to sleep waiting for requests")

        initThread()

        while(true)
        {
            val request = requestQueue.take()
            Timber.e("Woke up on request")
            if(request.quit) // quit on special request (this is how you exit a looping thread proper)
                break
            Timber.e("Processing request for page ${request.pageNo}")
            pdfRenderer?.let { renderer ->
                val currentPage = renderer.openPage(request.pageNo)
                currentPage?.let { page ->
                    Timber.e("Rendering pdf ${page.width}X${page.height}")
                    val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                    // Here, we render the page onto the Bitmap.
                    // To render a portion of the page, use the second and third parameter. Pass nulls to get
                    // the default result.
                    // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    page.close()
                }

            }
        }

        Timber.e("AsyncPdfRenderer thread quitting")
        shutdownThread()
    }

    fun initThread()
    {
        try {
            val file = File(context.cacheDir, filename)
            if (!file.exists()) {
                // Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
                // the cache directory.
                val asset = context.assets.open(filename)
                val output = FileOutputStream(file)
                val buffer = ByteArray(8192)
                var size: Int
                while (true) {
                    size = asset.read(buffer)
                    if (size == -1)
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
                Timber.e("Loaded pdf succesfully")
            }
        }
        catch (t : Throwable)
        {
            t.printStackTrace()
            return
        }
    }

    fun shutdownThread()
    {
        pdfRenderer?.close()
        fileDescriptor?.close()
    }

    fun start(filename : String)
    {
        this.filename = filename
        thread.start()
    }

    // call from UI thread to collect thread
    fun finish()
    {
        // put quit request in queue and join the thread
        val req = RenderRequest()
        req.quit = true
        requestQueue.put(req)
        thread.join()
    }

    // called from main thread
    fun requestPage(pageno : Int)
    {
        val req = RenderRequest()
        req.pageNo = pageno
        requestQueue.put(req)
    }
}