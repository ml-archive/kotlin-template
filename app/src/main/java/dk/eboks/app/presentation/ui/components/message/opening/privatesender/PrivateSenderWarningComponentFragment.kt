package dk.eboks.app.presentation.ui.components.message.opening.privatesender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_private_sender_warning_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class PrivateSenderWarningComponentFragment : BaseFragment(), PrivateSenderWarningComponentContract.View {

    @Inject
    lateinit var presenter : PrivateSenderWarningComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_private_sender_warning_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        openBtn.setOnClickListener {
            presenter.setShouldProceed(true)
            activity.finish()
        }
    }
}