package dk.eboks.app.presentation.ui.message.components.viewers.image

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
import kotlinx.android.synthetic.main.fragment_imageview_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ImageViewComponentFragment : BaseFragment(), ImageViewComponentContract.View, EmbeddedViewer, ViewerFragment {

    @Inject
    lateinit var presenter : ImageViewComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_imageview_component, container, false)
        Timber.e("onCreateView IMAGEVIEWER")
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        val settings = webView.getSettings()
        settings.setUseWideViewPort(true)
        settings.setLoadWithOverviewMode(true)
        settings.setBuiltInZoomControls(true)
        settings.setDisplayZoomControls(false)
        presenter.setup(arguments?.getString("URI"))
    }

    override fun showImage(filename : String) {
        Timber.e("Attempting to open $filename")
        val html = "<html><head></head><body style=\"background-color: #aaa; margin: 0px; padding: 0px\"><img src=\"file://$filename\" width=\"100%\"></body></html>"
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "")
    }

    override fun showImageURI(uri: String) {
        Timber.e("Attempting to open URI $uri")
        val file = FilePickerUriHelper.getFile(activity, uri)
        showImage(file.path)
    }

    override fun print() {
        webView.printAndForget(context)
        Timber.e("Print called")
    }
}