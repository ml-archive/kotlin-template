package dk.eboks.app.presentation.ui.message.components.viewers.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.exifinterface.media.ExifInterface
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
class ImageViewComponentFragment : BaseFragment(), ImageViewComponentContract.View, EmbeddedViewer,
    ViewerFragment {

    @Inject
    lateinit var presenter: ImageViewComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_imageview_component, container, false)
        Timber.e("onCreateView IMAGEVIEWER")
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        val settings = webView.settings
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        presenter.setup(arguments?.getString("URI"))
    }

    override fun showImage(filename: String) {
        val deg = getImageRotation(filename)
        Timber.e("Attempting to open $filename, rotate: $deg")
        val html =
            "<html>" +
                "<head></head>" +
                "<body style=\"background-image: linear-gradient(#294350, #537888);\n; margin: 0px; padding: 0px\"><img src=\"file://$filename\" width=\"100%\" style=\"${deg.toCssRotationTransform()}\" />" +
                "</body><" +
                "/html>"
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "")
    }

    private fun getImageRotation(filename: String): Int {
        val exifInterface = ExifInterface(filename)
        return when (exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private fun Int.toCssRotationTransform(): String {
        return if (this == 0) "" else "transform: rotate(${this}deg);"
    }

    override fun showImageURI(uri: String) {
        Timber.e("Attempting to open URI $uri")
        val file = FilePickerUriHelper.getFile(activity ?: return, uri)
        showImage(file.path)
    }

    override fun print() {
        webView.printAndForget(context ?: return)
        Timber.e("Print called")
    }
}