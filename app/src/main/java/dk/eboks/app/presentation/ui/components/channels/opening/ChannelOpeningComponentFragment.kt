package dk.eboks.app.presentation.ui.components.channels.opening

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_channel_opening_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelOpeningComponentFragment : BaseFragment(), ChannelOpeningComponentContract.View {

    val inflater by lazy { LayoutInflater.from(context) }

    @Inject
    lateinit var presenter : ChannelOpeningComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_opening_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun setupTranslations() {

    }

    override fun showOpenState(channel: Channel) {
        val v = inflater.inflate(R.layout.include_channel_detail_bottom_open, contentBottom, false)
        contentBottom.addView(v)
    }

    override fun showDisabledState(channel: Channel) {
        val v = inflater.inflate(R.layout.include_channel_detail_bottom_not_available, contentBottom, false)
        contentBottom.addView(v)
    }

    override fun showInstallState(channel: Channel) {
        val v = inflater.inflate(R.layout.include_channel_detail_bottom_install, contentBottom, false)
        contentBottom.addView(v)
    }
}