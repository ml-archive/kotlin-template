package dk.eboks.app.presentation.ui.message.components.viewers.pdf

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import java.io.File
import java.io.FileOutputStream

class PdfPrintAdapter(private val fileName: String) : PrintDocumentAdapter() {

    override fun onLayout(attrs: PrintAttributes?, attrs2: PrintAttributes?, cancelSignal: CancellationSignal?, resultCallback: LayoutResultCallback?, bundle: Bundle?) {
        if (cancelSignal?.isCanceled == true) {
            resultCallback?.onLayoutCancelled()
        } else {
            val info = PrintDocumentInfo.Builder(fileName)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build()
            resultCallback?.onLayoutFinished(info, attrs == attrs2)
        }
    }

    override fun onWrite(pageRanges: Array<out PageRange>?, fileDescriptor: ParcelFileDescriptor?, cancelSignal: CancellationSignal?, resultCallback: WriteResultCallback?) {
        val file = File(fileName)
        val out = FileOutputStream(fileDescriptor?.fileDescriptor)
        out.write(file.readBytes())
        resultCallback?.onWriteFinished( arrayOf(PageRange.ALL_PAGES))

    }
}