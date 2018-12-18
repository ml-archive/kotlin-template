package dk.eboks.app.presentation.ui.senders.screens.segment

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.senders.components.categories.CategoriesComponentFragment
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_senders_detail.*
import timber.log.Timber
import java.util.*
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

        senderDetailABL.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange + verticalOffset < 200) {
                senderDetailTB.title = segment.name
            } else {
                senderDetailTB.title = ""
            }
        }

        senderDetailRegisterTB.setOnCheckedChangeListener { buttonView, isChecked ->
            Timber.d("toggle")
            if (isChecked) {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_48_checkmark_white, 0)
            } else {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
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

    private fun updateHeader(segment: Segment) {
        senderDetailTB.title = segment.name
        senderDetailNameTv.text = segment.name
        senderDetailRegisterTB.visibility = View.VISIBLE

        Glide.with(this)
                .load(segment.image?.url)
                .apply(RequestOptions()
                        .fallback(R.drawable.icon_72_senders_private)
                        .placeholder(R.drawable.icon_72_senders_private)
                )
                .into(senderDetailIv)

        senderDetailRegisterTB.setOnTouchListener(View.OnTouchListener { v, event ->
            return@OnTouchListener when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if (senderDetailRegisterTB.isChecked) {
                        AlertDialog.Builder(this@SegmentDetailActivity)
                                .setTitle(Translation.senders.unregisterAlertTitle)
                                .setMessage(Translation.senders.unregisterAlertDescription)
                                .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                                    dialog.cancel()
                                }
                                .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->
                                    senderDetailRegisterTB.visibility = View.INVISIBLE
                                    presenter.unregisterSegment(segment.id)
                                    dialog.dismiss()
                                }
                                .show()
                    } else {
                        AlertDialog.Builder(this@SegmentDetailActivity)
                                .setTitle(Translation.senders.registerAlertTitle)
                                .setMessage(Translation.senders.registerAlertDescription)
                                .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                                    dialog.cancel()
                                }
                                .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->
                                    senderDetailRegisterTB.visibility = View.INVISIBLE
                                    presenter.registerSegment(segment.id)
                                    dialog.dismiss()
                                }
                                .show()
                    }
                    true
                }
                else -> {
                    v.onTouchEvent(event)
                }
            }
        })

        senderDetailRegisterTB.isChecked = segment.registered == 0
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

