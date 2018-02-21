package dk.eboks.app.presentation.ui.components.channels.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelDetailComponentFragment : BaseFragment(), ChannelDetailComponentContract.View {

    @Inject
    lateinit var presenter : ChannelDetailComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //todo should this fragment inflate all 4 fragments : fragment_channel_detail_open_component fragment_channel_detail_no_available_component fragment_channel_detail_nemid_component fragment_channel_detail_install_component
        val rootView = inflater?.inflate(R.layout.fragment_channel_detail_open_component, container, false)
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