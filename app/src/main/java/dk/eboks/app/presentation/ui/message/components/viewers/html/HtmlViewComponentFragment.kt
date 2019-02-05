package dk.eboks.app.presentation.ui.message.components.viewers.html

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.ViewerFragment
import dk.eboks.app.presentation.ui.message.components.viewers.base.EmbeddedViewer
import dk.eboks.app.util.printAndForget
import dk.nodes.filepicker.uriHelper.FilePickerUriHelper
import kotlinx.android.synthetic.main.fragment_htmlview_component.*
import timber.log.Timber
import java.io.File
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class HtmlViewComponentFragment : BaseFragment(), HtmlViewComponentContract.View, EmbeddedViewer,
    ViewerFragment {

    @Inject
    lateinit var presenter: HtmlViewComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_htmlview_component, container, false)
        Timber.e("onCreateView HTMLVIEWER")
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        val settings = webView.settings
        settings.loadWithOverviewMode = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        presenter.setup(arguments?.getString("URI"))
    }

    override fun showHtml(filename: String) {
        val content = File(filename).inputStream().readBytes().toString(Charsets.UTF_8)
        webView.loadDataWithBaseURL("", content, "text/html", "UTF-8", "")
    }

    override fun showHtmlURI(uriString: String) {
        Timber.e("Attempting to open URI $uriString")
        val file = FilePickerUriHelper.getFile(activity ?: return, uriString)
        showHtml(file.path)
    }

    override fun print() {
        Timber.e("Print called")
        webView.printAndForget(context ?: return)
    }
}