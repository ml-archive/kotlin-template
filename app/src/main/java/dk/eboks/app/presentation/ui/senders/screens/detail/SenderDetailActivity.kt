package dk.eboks.app.presentation.ui.senders.screens.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.senders.presentation.ui.screens.detail.SenderDetailContract
import dk.eboks.app.util.onClick
import dk.eboks.app.util.updateCheckDrawable
import dk.eboks.app.util.visible
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_senders_detail.*
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class SenderDetailActivity : BaseActivity(), SenderDetailContract.View {

    private var onLanguageChangedListener: (Locale) -> Unit = {
    }

    @Inject
    lateinit var presenter: SenderDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senders_detail)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        val sender = intent.getParcelableExtra<Sender>(Sender::class.simpleName)
        if (sender == null) {
            finish()
            return
        }

        updateHeader(sender)

        // pass your knowledge on to your siblings, so they in turn can use it and pass it on to their siblings...
        val b = Bundle()
        b.putParcelable(Sender::class.simpleName, sender)

        senderGroupsListComponentF.arguments = b
        senderDetailInfoF.arguments = b

        // translations
        NStack.addLanguageChangeListener(onLanguageChangedListener)

        senderDetailRegisterTB.textOn = Translation.senders.registered
        senderDetailRegisterTB.textOff = Translation.senders.register
        senderDetailRegisterTB.text = when (sender.registered) {
            0 -> Translation.senders.register
            else -> Translation.senders.registered
        }

        senderDetailRegisterTB.isChecked = sender.registered != 0

        senderDetailTB.setNavigationOnClickListener {
            finish()
        }

        senderDetailCTL.isTitleEnabled = false

        senderDetailABL.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange + verticalOffset < 200) {
                senderDetailTB.title = sender.name
            } else {
                senderDetailTB.title = ""
            }
        })

        senderDetailRegisterTB.setOnCheckedChangeListener { buttonView, isChecked ->
            Timber.d("toggle")
            buttonView.updateCheckDrawable()
        }

        presenter.loadSender(sender.id)
    }

    override fun showSender(sender: Sender) {
        // pass the knowledge on to your siblings, so they in turn can use it
        val b = Bundle()
        b.putParcelable(Sender::class.simpleName, sender)

        senderGroupsListComponentF.arguments = b
        senderDetailInfoF.arguments = b

        // also update the header
        updateHeader(sender)
    }

    private fun updateHeader(sender: Sender) {
        senderDetailTB.title = sender.name
        senderDetailNameTv.text = sender.name
        senderDetailRegisterTB.visibility = View.VISIBLE

        val iconFallback = if (sender.isPublic) R.drawable.icon_72_senders_public else R.drawable.icon_72_senders_private
        senderDetailBodyTv.visible = sender.isPublic


        Glide.with(this)
                .load(sender.logo?.url)
                .apply(RequestOptions()
                        .fallback(iconFallback)
                        .placeholder(iconFallback)
                )
                .into(senderDetailIv)

        senderDetailRegisterTB.onClick {
            if (senderDetailRegisterTB.isChecked) {
                AlertDialog.Builder(this@SenderDetailActivity)
                        .setTitle(Translation.senders.unregisterAlertTitle)
                        .setMessage(Translation.senders.unregisterAlertDescription)
                        .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                            dialog.cancel()
                        }
                        .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->
                            senderDetailRegisterTB.visibility = View.INVISIBLE
                            presenter.unregisterSender(sender.id)
                            dialog.dismiss()
                        }
                        .show()
            } else {
                AlertDialog.Builder(this@SenderDetailActivity)
                        .setTitle(Translation.senders.registerAlertTitle)
                        .setMessage(Translation.senders.registerAlertDescription)
                        .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                            dialog.cancel()
                        }
                        .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->
                            senderDetailRegisterTB.visibility = View.INVISIBLE
                            presenter.registerSender(sender.id)
                            dialog.dismiss()
                        }
                        .show()
            }
        }

        senderDetailRegisterTB.isChecked = sender.registered != 0
        senderDetailRegisterTB.updateCheckDrawable()

    }


    override fun onDestroy() {
        NStack.removeLanguageChangeListener(onLanguageChangedListener)
        super.onDestroy()
    }

    override fun showSuccess() {
        senderDetailRegisterTB.visibility = View.VISIBLE
        senderDetailRegisterTB.toggle()
    }

    override fun showError(message: String) {
        senderDetailRegisterTB.visibility = View.VISIBLE
    }

    override fun toggleLoading(enabled: Boolean) {
        senderDetailRegisterTB.visible = !enabled
        senderDetailContainer.visible = !enabled
        progressBar.visible = enabled
    }
}
