package dk.eboks.app.presentation.ui.components.message.viewers.html

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_htmlview_component.*
import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class HtmlViewComponentFragment : BaseFragment(), HtmlViewComponentContract.View {

    @Inject
    lateinit var presenter : HtmlViewComponentContract.Presenter

    internal var data: ByteArray? = null
    internal var name: String = ""
    internal var format: String = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_htmlview_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        readArguments()
    }

    override fun setupTranslations() {

    }

    private fun readArguments() {
        val args = arguments
        if (args == null) {
            Timber.e("HTMLViewer received no arguments, not much to do")
            return
        }
        name = args.getString("name")
        format = args.getString("format")
        data = args.getByteArray("data")
        show()
    }

    private fun show() {
        val settings = webView.getSettings()
        settings.setLoadWithOverviewMode(true)
        settings.setBuiltInZoomControls(true)
        settings.setDisplayZoomControls(false)

        var mimetype = "text/plain"
        if (format.contentEquals("HTM") || format.contentEquals("HTML"))
            mimetype = "text/html"
        var html = "_No data"


        try {
            html = String(data!!, Charset.forName("ISO-8859-1")) // has been encoded with iso-8859-1 from server
        } catch (e: UnsupportedEncodingException) {
            Timber.e(e)
        }

        html = html.replace("charset=iso-8859-1", "charset=utf-8")

        // html header has encoding inside the content type, so the encoding must be appended in the content type field
        webView.loadData(html, mimetype + "; charset=utf-8", null)
    }
}