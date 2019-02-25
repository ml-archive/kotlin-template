package dk.eboks.app.presentation.ui.message.screens.reply

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import dk.eboks.app.R
import dk.eboks.app.domain.models.formreply.FormInput
import dk.eboks.app.mail.presentation.ui.message.reply.ReplyFormInput

class LinkFormInput(formInput: FormInput, inflater: LayoutInflater, handler: Handler) :
    ReplyFormInput(formInput, inflater, handler) {
    var linkBtn: Button? = null

    init {
        isValid = true
    }

    override fun buildView(vg: ViewGroup): View {
        val v = inflater.inflate(R.layout.form_input_link, vg, false)
        linkBtn = v.findViewById(R.id.linkBtn)
        linkBtn?.text = formInput.label
        linkBtn?.setOnClickListener {
            formInput.value?.let { openUrlExternal(it) }
        }
        return v
    }

    private fun openUrlExternal(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (intent.resolveActivity(inflater.context.packageManager) != null) {
            startActivity(inflater.context, intent, null)
        }
    }
}