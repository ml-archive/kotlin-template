package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import javax.inject.Inject

/**
 * Created by Christian on 5/14/2018.
 * @author   Christian
 * @since    5/14/2018.
 */
class StoreboxSignInFragment : BaseFragment() {

    @Inject
    lateinit var presenter: ConnectStoreboxContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        return inflater?.inflate( R.layout.fragment_channel_storebox_detail_component, container, false )
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

    }
}