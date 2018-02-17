package dk.eboks.app.presentation.ui.components.message.viewers.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.message.viewers.base.EmbeddedViewer
import kotlinx.android.synthetic.main.fragment_imageview_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ImageViewComponentFragment : BaseFragment(), ImageViewComponentContract.View, EmbeddedViewer {

    @Inject
    lateinit var presenter : ImageViewComponentContract.Presenter

    internal var imageData: ByteArray? = null
    internal var imageName: String = ""
    internal var imageFormat: String = ""
    internal var stringUri: String = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_imageview_component, container, false)
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
        /*
        val args = arguments
        if (args == null) {
            Timber.e("Imageviewer received no arguments, not much to do")
            return
        }
        imageName = args.getString("name")
        imageFormat = args.getString("format")
        imageData = args.getByteArray("imageData")
        //stringUri = args.getString("uri")
        */
        stringUri = "https://picsum.photos/1024/1024"
        Timber.e("image uri: " + stringUri)
        showImage()

    }

    private fun showImage() {
        val settings = webView.getSettings()
        settings.setUseWideViewPort(true)
        settings.setLoadWithOverviewMode(true)
        settings.setBuiltInZoomControls(true)
        settings.setDisplayZoomControls(false)

        val html = "<html><head></head><body><img src=\"$stringUri\" width=\"100%\"></body></html>"
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "")
    }

}