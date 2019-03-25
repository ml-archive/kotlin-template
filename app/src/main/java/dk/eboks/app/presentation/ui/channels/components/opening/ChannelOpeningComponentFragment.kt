package dk.eboks.app.presentation.ui.channels.components.opening

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.components.content.storebox.content.ChannelContentStoreboxComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.web.ChannelContentComponentFragment
import dk.eboks.app.presentation.ui.channels.components.requirements.ChannelRequirementsComponentFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentActivity
import dk.eboks.app.presentation.ui.channels.screens.content.storebox.ConnectStoreboxActivity
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.widgets.GlideAlphaTransform
import dk.eboks.app.util.BundleKeys
import dk.eboks.app.util.ChannelType
import dk.eboks.app.util.guard
import dk.eboks.app.util.putArg
import dk.eboks.app.util.type
import androidx.core.view.isVisible
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_channel_opening_component.*
import kotlinx.android.synthetic.main.include_channel_detail_bottom_install.*
import kotlinx.android.synthetic.main.include_channel_detail_top.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class ChannelOpeningComponentFragment : BaseFragment(), ChannelOpeningComponentContract.View {

    val inflater by lazy { LayoutInflater.from(context) }

    @Inject
    lateinit var presenter: ChannelOpeningComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_channel_opening_component,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        arguments?.getParcelable<Channel>(BundleKeys.channel)?.let {
            presenter.setup(it.id)
            setupToolbar(it)
        }.guard {
            // activity?.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        clearFindViewByIdCache()
        presenter.refreshChannel()
    }

    private fun setupToolbar(channel: Channel) {
        mainTb.title = channel.name
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            activity?.onBackPressed()
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
                Glide.with(context ?: return).load(url).apply(
                    RequestOptions.bitmapTransform(
                        GlideAlphaTransform(it.color)
                    )
                ).into(backgroundIv)
            }.guard {
                Glide.with(context ?: return).load(url).into(backgroundIv)
            }
        }

        channel.logo?.let {
            Glide.with(context ?: return).load(it.url).into(logoIv)
        }
    }

    override fun showOpenState(channel: Channel) {
        Timber.i("showOpenState ${channel.name}")
        val v = inflater.inflate(R.layout.include_channel_detail_bottom_open, contentBottom, false)
        setupTopView(channel)
        contentBottom.addView(v)
        val button = v.findViewById<Button>(R.id.openBtn)
        button?.text = Translation.channels.openChannel
        val colorTint = Color.parseColor(channel.background.rgb)
        button.backgroundTintList = ColorStateList.valueOf(colorTint)
        button?.setOnClickListener {
            presenter.open(channel)
        }
    }

    override fun showDisabledState(channel: Channel) {
        Timber.i("showDisabledState ${channel.name}")
        val v = inflater.inflate(
            R.layout.include_channel_detail_bottom_not_available,
            contentBottom,
            false
        )
        setupTopView(channel)
        contentBottom.addView(v)
    }

    override fun showInstallState(channel: Channel) {
        Timber.i("showInstallState ${channel.name}")
        contentBottom.removeAllViews()

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
            if (channel.type == ChannelType.Storebox) { // notify about storebox one last time...
                showStoreboxConfirmDialog(channel)
            } else {
                ChannelControlComponentFragment.refreshOnResume = true
                presenter.install(channel)
            }
        }
        if (channel.type == ChannelType.Storebox) {
            linkStoreboxBtn.visibility = View.VISIBLE
            linkStoreboxBtn.setOnClickListener {
                ChannelControlComponentFragment.refreshOnResume = true
                startActivity(Intent(context, ConnectStoreboxActivity::class.java))
            }
        }
    }

    override fun showVerifyState(channel: Channel, provider: LoginProvider) {
        Timber.i("showVerifyState ${channel.name}")
        contentBottom.removeAllViews()
        val v = inflater.inflate(
            R.layout.include_channel_detail_bottom_verify,
            contentBottom,
            false
        )
        setupTopView(channel)
        contentBottom.addView(v)
        val button = v.findViewById<Button>(R.id.verifyBtn)
        button.text = Translation.channels.logOnWithPKI.replace("[pkiName]", provider.name)
        v.findViewById<TextView>(R.id.descriptionTv)?.text = Translation.channels.verifyYourProfile
        var colorTint = Color.BLUE // default color
        var txtcolor = channel.background.rgb ?: ""
        if (!txtcolor.contains('#')) {
            txtcolor = "#$txtcolor"
        }
        try {
            colorTint = Color.parseColor(txtcolor)
        } catch (t: Throwable) {
        }
        button.backgroundTintList = ColorStateList.valueOf(colorTint)
        button?.setOnClickListener {
            // presenter.install(channel)
            VerificationComponentFragment.verificationSucceeded = false
            getBaseActivity()?.openComponentDrawer(VerificationComponentFragment::class.java)
        }
    }

    override fun showStoreboxUserAlreadyExists() {
        Timber.i("show already exists")
        AlertDialog.Builder(context ?: return)
            .setMessage(Translation.storeboxlogin.errorAlreadyExistsMessage)
            .setPositiveButton(Translation.storeboxlogin.signInButton) { dialog, which ->
                startActivity(Intent(context, ConnectStoreboxActivity::class.java))
            }
            .setNegativeButton(Translation.defaultSection.ok) { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showStoreboxConfirmDialog(channel: Channel) {
        Timber.i("showStoreboxConfirmDialog")
        AlertDialog.Builder(context ?: return)
            .setMessage(Translation.storeboxlogin.createUserButton)
            .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->
                ChannelControlComponentFragment.refreshOnResume = true
                presenter.install(channel)
            }
            .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showProgress(show: Boolean) {
        progress.isVisible = show
    }

    override fun showRequirementsDrawer(channel: Channel) {
        Timber.i("showRequirementsDrawer ${channel.name}")
        channel.requirements?.let {
            val data = Bundle()
            data.putParcelable("channel", channel)
            getBaseActivity()?.openComponentDrawer(
                ChannelRequirementsComponentFragment::class.java,
                data
            )
        }.guard {
            showOpenState(channel)
        }
    }

    override fun openChannelContent(channel: Channel) {
        Timber.i("openChannelContent ${channel.name}")
        val fragment =
            ChannelContentComponentFragment().putArg(Channel::class.simpleName!!, channel)
        getBaseActivity()?.addFragmentOnTop(R.id.content, fragment, false)
    }

    override fun openStoreBoxContent(channel: Channel) {
        Timber.i("openStoreBoxContent ${channel.name}")
        val fragment = ChannelContentStoreboxComponentFragment().putArg(
            Channel::class.java.simpleName,
            channel
        )
        getBaseActivity()?.addFragmentOnTop(R.id.content, fragment, false)
    }

    override fun openEkeyContent(channel: Channel) {
        Timber.i("openEkeyContent")
        val intent = Intent(context, EkeyContentActivity::class.java)
        intent.putExtra("channel", channel)
        startActivity(intent)
        activity?.finish()
    }

    companion object {
        @JvmStatic
        fun newInstance(channel: Channel): ChannelOpeningComponentFragment {
            return ChannelOpeningComponentFragment().apply {
                arguments = bundleOf(BundleKeys.channel to channel)
            }
        }
    }
}