package dk.eboks.app.presentation.ui.components.message.protectedmessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.pasta.fragment.PastaComponentContract
import dk.eboks.app.presentation.base.BaseFragment
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ProtectedMessageComponentFragment : BaseFragment(), ProtectedMessageComponentContract.View {

    @Inject
    lateinit var presenter : ProtectedMessageComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_protected_message_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun setupTranslations() {

    }

}