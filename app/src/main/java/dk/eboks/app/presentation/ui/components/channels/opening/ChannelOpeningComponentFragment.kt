package dk.eboks.app.presentation.ui.components.channels.opening

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentComponentFragment
import dk.eboks.app.presentation.ui.components.channels.requirements.ChannelRequirementsComponentFragment
import dk.eboks.app.presentation.ui.screens.channels.content.ekey.EkeyContentActivity
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.ConnectStoreboxActivity
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.StoreboxContentActivity
import dk.eboks.app.presentation.widgets.GlideAlphaTransform
import dk.eboks.app.util.getType
import dk.eboks.app.util.guard
import kotlinx.android.synthetic.main.fragment_channel_opening_component.*
import kotlinx.android.synthetic.main.include_channel_detail_bottom_install.*
import kotlinx.android.synthetic.main.include_channel_detail_top.*
import javax.inject.Inject

class ChannelOpeningComponentFragment : BaseFragment(), ChannelOpeningComponentContract.View {

    val inflater by lazy { LayoutInflater.from(context) }

    @Inject
    lateinit var presenter: ChannelOpeningComponentContract.Presenter

    var refreshChannel = false

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(
                R.layout.fragment_channel_opening_component,
                container,
                false
        )
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun onResume() {
        super.onResume()
        if (refreshChannel) {
            presenter.refreshChannel()
        }
    }


    private fun setupTopView(channel: Channel) {
        headerTv.text = channel.payoff
        nameTv.text = channel.name
        nameTv.setTextColor(channel.background.color)
        channel.description?.let { descriptionTv.text = it }

        channel.background.let {
            backgroundIv.setBackgroundColor(it.color)
        }

        channel.image?.let {
            val url = it.url

            channel.background.let {
                Glide.with(context).load(url).apply(
                        RequestOptions.bitmapTransform(
                                GlideAlphaTransform(it.color)
                        )
                ).into(backgroundIv)
            }.guard {
                Glide.with(context).load(url).into(backgroundIv)
            }
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
        val colorTint = Color.parseColor(channel.background?.rgb)
        button.backgroundTintList = ColorStateList.valueOf(colorTint)
        button?.setOnClickListener {
            presenter.open(channel)
        }
    }

    override fun showDisabledState(channel: Channel) {
        val v = inflater.inflate(
                R.layout.include_channel_detail_bottom_not_available,
                contentBottom,
                false
        )
        setupTopView(channel)
        contentBottom.addView(v)
    }

    override fun showInstallState(channel: Channel) {
        val v = inflater.inflate(
                R.layout.include_channel_detail_bottom_install,
                contentBottom,
                false
        )
        setupTopView(channel)
        contentBottom.addView(v)

        installBtn?.text = Translation.channels.installChannel
        val colorTint = channel.background.color
        installBtn.backgroundTintList = ColorStateList.valueOf(colorTint)
        installBtn?.setOnClickListener {
            presenter.install(channel)
            refreshChannel = true
        }
        if (channel.getType() == "storebox") {
            linkStoreboxBtn.visibility = View.VISIBLE
            linkStoreboxBtn.setOnClickListener {
                startActivity(Intent(context, ConnectStoreboxActivity::class.java))
            }
        }
    }

    override fun showVerifyState(channel: Channel, provider: LoginProvider) {
        contentBottom.removeAllViews()
        val v = inflater.inflate(
                R.layout.include_channel_detail_bottom_verify,
                contentBottom,
                false
        )
        setupTopView(channel)
        contentBottom.addView(v)
        val button = v.findViewById<Button>(R.id.verifyBtn)
        v.findViewById<TextView>(R.id.descriptionTv)?.text = Translation.channels.verifyYourProfile
        val colorTint = Color.parseColor(channel.background?.rgb)
        button.backgroundTintList = ColorStateList.valueOf(colorTint)
        button?.setOnClickListener {
            presenter.install(channel)
        }
    }

    override fun goToWebView(channel: Channel) {
        presenter.open(channel)
    }

    override fun showProgress(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun openChannelContent() {
        val fragment = ChannelContentComponentFragment()
        val args = Bundle()
        // todo API call: '/channels/{channelId}/content/link'  returns a string with the url
        args.putString(Channel::class.simpleName, "file:///android_asset/index.html")
        fragment.arguments = args
        getBaseActivity()?.addFragmentOnTop(R.id.content, fragment, false)
    }

    override fun showVerifyDrawer(channel: Channel) {
        channel.requirements?.let {
            val data = Bundle()
            data.putSerializable("channel", channel)
            getBaseActivity()?.openComponentDrawer(
                    ChannelRequirementsComponentFragment::class.java,
                    data
            )
        }.guard {
            showOpenState(channel)
        }
    }

    override fun openStoreBoxContent() {
        startActivity(Intent(context, StoreboxContentActivity::class.java))
        activity.finish()
    }

    override fun openEkeyContent() {
        startActivity(Intent(context, EkeyContentActivity::class.java))
        activity.finish()
    }
}