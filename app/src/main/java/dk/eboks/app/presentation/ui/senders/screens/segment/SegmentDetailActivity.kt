package dk.eboks.app.presentation.ui.senders.screens.segment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.senders.components.categories.CategoriesComponentFragment
import dk.eboks.app.util.onClick
import dk.eboks.app.util.showCheckedDrawable
import dk.eboks.app.util.visible
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_senders_detail.*
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class SegmentDetailActivity : BaseActivity(), SegmentDetailContract.View {

    var onLanguageChangedListener: (Locale) -> Unit = { }

    @Inject
    lateinit var presenter: SegmentDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senders_detail)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        val segment = intent.getParcelableExtra<Segment>(Segment::class.simpleName)
        if (segment == null) {
            finish()
            return
        }

        senderDetailContainer.removeAllViews()
        updateHeader(segment)

        //translations
        NStack.addLanguageChangeListener(onLanguageChangedListener)

        senderDetailRegisterTB.textOn = Translation.senders.registered
        senderDetailRegisterTB.textOff = Translation.senders.register

        senderDetailBodyTv.visibility = View.GONE // only for public authorities
        senderDetailRegisterTB.isChecked = segment.registered != 0

        senderDetailTB.setNavigationOnClickListener {
            finish()
        }

        senderDetailCTL.isTitleEnabled = false
        senderDetailABL.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange + verticalOffset < 200) {
                senderDetailTB.title = segment.name
            } else {
                senderDetailTB.title = ""
            }
        })

        senderDetailRegisterTB.setOnCheckedChangeListener { buttonView, isChecked ->
            Timber.d("toggle")
            buttonView.showCheckedDrawable()
        }

        presenter.loadSegment(segment.id)
    }

    override fun showSegment(segment: Segment) {
        senderDetailContainer.removeAllViews()

        // pass the knowledge on...
        val b = Bundle()
        b.putParcelable(Segment::class.simpleName, segment)

        val frag = CategoriesComponentFragment()
        frag.arguments = b
        supportFragmentManager.beginTransaction()
                .add(senderDetailContainer.id, frag)
                .commit()

        // also update the header
        updateHeader(segment)

    }

    override fun toggleLoading(enabled: Boolean) {
        senderDetailRegisterTB.visible = !enabled
        senderDetailContainer.visible = !enabled
        progressBar.visible = enabled
    }

    private fun updateHeader(segment: Segment) {
        senderDetailTB.title = segment.name
        senderDetailNameTv.text = segment.name
        senderDetailRegisterTB.visibility = View.VISIBLE

        if (segment.type == "public") {
            senderDetailBodyTv.visibility = View.VISIBLE
            senderDetailBodyTv.text = if (segment.registered != 0) Translation.senderdetails.publicAuthoritiesRegisteredDescription
            else Translation.senderdetails.publicAuthoritiesUnregisteredDescription
        }

        Glide.with(this)
                .load(segment.image?.url)
                .apply(RequestOptions()
                        .fallback(R.drawable.icon_72_senders_private)
                        .placeholder(R.drawable.icon_72_senders_private)
                )
                .into(senderDetailIv)



        senderDetailRegisterTB.onClick {
            when {
                segment.type == "public" -> {
                    AlertDialog.Builder(this)
                            .setTitle(Translation.senders.cannotUnregister)
                            .setMessage(Translation.senders.cannotUnregisterPublicDescription)
                            .setPositiveButton(Translation.defaultSection.ok) { d, _ -> d.dismiss() }
                            .show()
                }
                senderDetailRegisterTB.isChecked ->
                    showConfirmationDialog(Translation.senders.unregisterAlertTitle, Translation.senders.unregisterAlertDescription) { dialog ->
                    senderDetailRegisterTB.visibility = View.INVISIBLE
                    presenter.unregisterSegment(segment.id)
                    dialog.dismiss()
                }
                else -> showConfirmationDialog(Translation.senders.registerAlertTitle, Translation.senders.registerAlertDescription) { dialog ->
                    senderDetailRegisterTB.visibility = View.INVISIBLE
                    presenter.registerSegment(segment.id)
                    dialog.dismiss()
                }
            }
        }

        senderDetailRegisterTB.isChecked = segment.registered != 0
        senderDetailRegisterTB.showCheckedDrawable()

    }

    private fun showConfirmationDialog(title: String, message: String, onConfirm: (DialogInterface) -> Unit) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(Translation.defaultSection.ok) { dialog, _ -> onConfirm(dialog) }
                .setNegativeButton(Translation.defaultSection.cancel) { dialog, _ -> dialog.cancel() }
                .show()
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
}

