package dk.eboks.app.presentation.ui.components.channels.opening

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.ViewErrorController
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentComponentFragment
import kotlinx.android.synthetic.main.fragment_channel_opening_component.*
import kotlinx.android.synthetic.main.include_channel_detail_top.*
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

    private fun setupTopView(channel: Channel)
    {
        headerTv.text = channel.payoff
        nameTv.text = channel.name
        nameTv.setTextColor(Color.parseColor(channel.background?.rgba))
        channel.description?.let { descriptionTv.text = it.text }
        channel.background?.let {
            backgroundIv.setColorFilter(Color.parseColor(it.rgba), PorterDuff.Mode.OVERLAY)
        }

        channel.image?.let {
            Glide.with(context).load(it.url).into(backgroundIv)

        }
        channel.logo?.let {
            Glide.with(context).load(it.url).into(logoIv)
        }
    }

    override fun showOpenState(channel: Channel) {
        val v = inflater.inflate(R.layout.include_channel_detail_bottom_open, contentBottom, false)
        setupTopView(channel)
        contentBottom.addView(v)
        val button = v.findViewById<Button>(R.id.openBtn)
        button?.text = Translation.channels.openChannel

        button?.setOnClickListener {
            presenter.open(channel)
        }
    }

    override fun showDisabledState(channel: Channel) {
        val v = inflater.inflate(R.layout.include_channel_detail_bottom_not_available, contentBottom, false)
        setupTopView(channel)
        contentBottom.addView(v)
    }

    override fun showInstallState(channel: Channel) {
        val v = inflater.inflate(R.layout.include_channel_detail_bottom_install, contentBottom, false)
        setupTopView(channel)
        contentBottom.addView(v)
        val button = v.findViewById<Button>(R.id.installBtn)
        button?.text = Translation.channels.installChannel
        button?.setOnClickListener {
            Config.getLoginProvider("nemid")?.let {
                presenter.install(channel, it)
            }
        }
    }

    override fun showVerifyState(channel: Channel, provider : LoginProvider) {
        contentBottom.removeAllViews()
        val v = inflater.inflate(R.layout.include_channel_detail_bottom_verify, contentBottom, false)
        contentBottom.addView(v)
        val button = v.findViewById<Button>(R.id.verifyBtn)
        v.findViewById<TextView>(R.id.descriptionTv)?.text = Translation.channels.verifyYourProfile
        button?.setOnClickListener {
            presenter.open(channel)
        }
    }

    override fun showProgress(show: Boolean) {
        progress.visibility = if(show) View.VISIBLE else View.GONE
    }

    override fun openChannelContent() {
        val fragment = ChannelContentComponentFragment()
        getBaseActivity()?.addFragmentOnTop(R.id.content, fragment, false)
        /*
        fragment?.let{
            fragmentManager.beginTransaction().add(R.id.content, it, ChannelContentComponentFragment::class.java.simpleName).commit()
        }
        */
    }
}