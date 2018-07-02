package dk.eboks.app.presentation.ui.components.message.viewers.html

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.message.viewers.base.EmbeddedViewer
import kotlinx.android.synthetic.main.fragment_htmlview_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class HtmlViewComponentFragment : BaseFragment(), HtmlViewComponentContract.View, EmbeddedViewer {

    @Inject
    lateinit var presenter : HtmlViewComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_htmlview_component, container, false)
        Timber.e("onCreateView HTMLVIEWER")
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        val settings = webView.getSettings()
        settings.setLoadWithOverviewMode(true)
        settings.setBuiltInZoomControls(true)
        settings.setDisplayZoomControls(false)

    }

    override fun showHtml(filename: String) {
        webView.loadUrl("file://$filename")
    }
}