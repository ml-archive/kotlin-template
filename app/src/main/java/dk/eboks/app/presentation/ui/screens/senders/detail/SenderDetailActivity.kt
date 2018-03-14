package dk.eboks.app.presentation.ui.screens.senders.detail

import android.os.Bundle
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_senders_detail.*

class SenderDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senders_detail)

        val sender = intent.getSerializableExtra(Sender::class.simpleName) as Sender?
        if (sender == null) {
            finish()
        } else {
            senderDetailTB.title = sender.name
            senderDetailNameTv.text = sender.name
            senderDetailBodyTv.text = sender.name + " body description"
            Glide.with(this).load(sender.logo).into(senderDetailIv)
        }

        senderDetailTB.title = ""
        senderDetailTB.setNavigationOnClickListener {
            finish()
        }

        senderDetailCTL.isTitleEnabled = false

        senderDetailABL.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange + verticalOffset < 200) {
                senderDetailTB.title = sender!!.name
            } else {
                senderDetailTB.title = "" // careful there should a space between double quote otherwise it wont work
            }
        }

        senderDetailRegisterTB.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_48_checkmark_white, 0)
            } else {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }


    }

    override fun setupTranslations() {
        // TODO add real translation
        senderDetailRegisterTB.text = "Register"
        senderDetailRegisterTB.textOn = "Register"
        senderDetailRegisterTB.textOff = "Register"
    }
}
