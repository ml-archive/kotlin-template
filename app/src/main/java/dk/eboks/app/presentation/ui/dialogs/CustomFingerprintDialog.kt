package dk.eboks.app.presentation.ui.dialogs

import android.content.Context
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.annotation.StyleRes
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.nodes.locksmith.core.fingerprint.FingerprintAlertDialogBase
import dk.nodes.locksmith.core.models.FingerprintDialogEvent
import dk.nodes.locksmith.core.models.OnFingerprintDialogEventListener
import kotlinx.android.synthetic.main.dialog_custom_fingerprint.*
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.M)
class CustomFingerprintDialog(context: Context) : FingerprintAlertDialogBase(context) {
    // Handler
    private val handler = Handler()

    var onUsePasswordBtnListener: (() -> Unit)? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        dialogCustomFingerprintBtnUsePassword.setOnClickListener {
            onUsePasswordBtnListener?.invoke()
        }

        dialogCustomFingerprintBtnCancel?.setOnClickListener {
            onCancelClicked()
            //closeDialog()
        }
    }

    override fun getDialogLayout(): Int {
        return R.layout.dialog_custom_fingerprint
    }

    override fun onFingerprintHelp(help: String) {
        setTvMessageWithStyle(help, dk.nodes.locksmith.R.style.FingerprintDialogWarn)
    }

    override fun onFingerprintError() {
        setTvMessageWithStyle(
                Translation.androidfingerprint.errorMessage,
                dk.nodes.locksmith.R.style.FingerprintDialogError
        )

        dialogCustomFingerprintBtnCancel.isEnabled = false
        dialogCustomFingerprintBtnUsePassword.isEnabled = false

        handler.postDelayed({ closeDialog() }, 1000)
    }

    override fun onFingerprintSuccess() {
        setTvMessageWithStyle(
                Translation.androidfingerprint.successMessage,
                dk.nodes.locksmith.R.style.FingerprintDialogSuccess
        )

        dialogCustomFingerprintBtnCancel.isEnabled = false
        dialogCustomFingerprintBtnUsePassword.isEnabled = false

        handler.postDelayed({ closeDialog() }, 1000)
    }

    private fun setTvMessageWithStyle(message: String, @StyleRes styleRes: Int) {
        dialogCustomFingerprintTvMessage.visibility = View.VISIBLE
        dialogCustomFingerprintTvMessage.text = message
        dialogCustomFingerprintTvMessage.setTextAppearance(styleRes)
    }

    fun setOnFingerprintDialogEventListener(listener: (FingerprintDialogEvent) -> Unit) {
        this.onFingerprintDialogEventListener = OnFingerprintDialogEventListener {
            listener(it)
        }
    }
}