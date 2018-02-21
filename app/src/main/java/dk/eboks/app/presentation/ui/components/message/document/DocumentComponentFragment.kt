package dk.eboks.app.presentation.ui.components.message.document

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.models.Message
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.FileUtils
import kotlinx.android.synthetic.main.fragment_document_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class DocumentComponentFragment : BaseFragment(), DocumentComponentContract.View {
    @Inject
    lateinit var presenter : DocumentComponentContract.Presenter

    @Inject
    lateinit var formatter : EboksFormatter

    @Inject
    lateinit var uiManager: UIManager

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_document_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun setupTranslations() {
    }

    override fun updateView(message: Message) {
        if(message.content == null)
        {
            componentRoot.visibility = View.GONE
            return
        }
        message.content?.let { content->
            nameTv.text = content.title
            sizeTv.text = formatter.formatSize(content)
            bodyLl.setOnClickListener {
                presenter.openExternalViewer(message)
            }
        }
    }

    override fun openExternalViewer(filename: String, mimeType : String) {
        FileUtils.openExternalViewer(context, filename, mimeType)
    }
}