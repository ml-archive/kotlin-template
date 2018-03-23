package dk.eboks.app.presentation.ui.screens.senders.detail

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_senders_detail.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SenderDetailActivity : BaseActivity(), SenderDetailContract.View {

    var onLanguageChangedListener: (Locale) -> Unit = {

    }

    @Inject
    lateinit var presenter: SenderDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senders_detail)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        val sender = intent.getSerializableExtra(Sender::class.simpleName) as Sender?
        if (sender == null) {
            finish()
            return
        }

        updateHeader(sender)

        // pass the knowledge on to your siblings, so they in turn can use it
        val b = Bundle()
        b.putSerializable(Sender::class.simpleName, sender)

        senderGroupsListComponentF.arguments = b
        senderDetailInfoF.arguments = b

        presenter.loadSender(sender.id)

        //translations
        NStack.addLanguageChangeListener(onLanguageChangedListener)
        senderDetailRegisterTB.text = Translation.senderdetails.register
        senderDetailRegisterTB.textOn = Translation.senderdetails.register
        senderDetailRegisterTB.textOff = Translation.senderdetails.registeredTypeYes

        senderDetailBodyTv.visibility = View.GONE // only for public authorities

        senderDetailTB.setNavigationOnClickListener {
            finish()
        }

        senderDetailCTL.isTitleEnabled = false

        senderDetailABL.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange + verticalOffset < 200) {
                senderDetailTB.title = sender.name
            } else {
                senderDetailTB.title = ""
            }
        }

    }

    override fun showSender(sender: Sender) {
        // pass the knowledge on to your siblings, so they in turn can use it
        val b = Bundle()
        b.putSerializable(Sender::class.simpleName, sender)

        senderGroupsListComponentF.arguments = b
        senderDetailInfoF.arguments = b

        // also update the header
        updateHeader(sender)
    }

    private fun updateHeader(sender: Sender) {
        senderDetailTB.title = sender.name
        senderDetailNameTv.text = sender.name

        Glide.with(this)
                .load(sender.logo?.url)
                .apply(RequestOptions()
                        .fallback(R.drawable.icon_72_senders_private)
                        .placeholder(R.drawable.icon_72_senders_private)
                )
                .into(senderDetailIv)

        senderDetailRegisterTB.setOnTouchListener(View.OnTouchListener { v, event ->
            return@OnTouchListener when(event.action) {
                MotionEvent.ACTION_UP-> {
                    if (senderDetailRegisterTB.isChecked) {
                        // TODO DO THE UNREGISTER CASE
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
                     true
                }
               else-> {
                   v.onTouchEvent(event)
               }
            }
        })


        senderDetailRegisterTB.setOnCheckedChangeListener { buttonView, isChecked ->
            Timber.d("toggle")
            if (isChecked) {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_48_checkmark_white, 0)
            } else {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
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

