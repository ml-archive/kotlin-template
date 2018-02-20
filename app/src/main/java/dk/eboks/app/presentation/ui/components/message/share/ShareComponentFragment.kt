package dk.eboks.app.presentation.ui.components.message.share

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.models.Message
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_share_component.*
import timber.log.Timber
import java.io.File
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

    override fun setupTranslations() {
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
        Timber.e("Opening share $filename $mimeType")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", File(filename))
        intent.setDataAndType(uri, mimeType)
        Timber.e("URI $uri")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(intent, "_Open with"))
        } else {
            Timber.e("Could not resolve share intent")
        }
    }
}