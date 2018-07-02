package dk.eboks.app.presentation.ui.components.message.detail.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.FileUtils
import kotlinx.android.synthetic.main.fragment_share_component.*
import javax.inject.Inject


/**
 * Created by bison on 09-02-2018.
 */
class ShareComponentFragment : BaseFragment(), ShareComponentContract.View {
    @Inject
    lateinit var presenter : ShareComponentContract.Presenter

    @Inject
    lateinit var uiManager: UIManager

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_share_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun updateView(message: Message) {
        if(message.content == null)
        {
            componentRoot.visibility = View.GONE
            return
        }
        message.content?.let { content->
            bodyLl.setOnClickListener {
                presenter.openExternalViewer(message)
            }
        }
    }

    override fun openExternalViewer(filename: String, mimeType : String) {
        FileUtils.openExternalViewer(context, filename, mimeType)
    }
}