package dk.eboks.app.presentation.ui.channels.components.content.ekey.pin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.ui.channels.components.content.ekey.BaseEkeyFragment
import dk.eboks.app.presentation.ui.overlay.screens.ButtonType
import dk.eboks.app.presentation.ui.overlay.screens.OverlayActivity
import dk.eboks.app.presentation.ui.overlay.screens.OverlayButton
import dk.eboks.app.util.onImeActionDone
import dk.eboks.app.util.onTextChanged
import kotlinx.android.synthetic.main.fragment_channel_ekey_pin.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyPinComponentFragment : BaseEkeyFragment(), EkeyPinComponentContract.View {

    @Inject
    lateinit var presenter: EkeyPinComponentContract.Presenter

    var handler = Handler()

    private val isCreate: Boolean
        get() = arguments?.getBoolean(ARG_IS_CREATE, false) ?: false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_channel_ekey_pin, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupListeners()
        setupTopbar(isCreate)
        setupTexts(isCreate)
    }

    private fun setupTexts(isCreate: Boolean) {
        ekeyPasswordInputLayout.hint = Translation.ekey.insertPasswordHint
        pinHeaderTv.text = when (isCreate) {
            true -> Translation.ekey.createEKey
            false -> Translation.ekey.pinCode
        }
    }

    private fun setupTopbar(isCreate: Boolean) {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.title = when (isCreate) {
            true -> Translation.ekey.createEKey
            false -> Translation.ekey.pinCode
        }

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    private fun setupListeners() {
        showKeyboard()

        ekeyPasswordInputEt.onImeActionDone {
            if (isCreate) {
                if (ekeyPasswordInputEt.text.toString().length >= 6) {
                    confirmPinAndFinish()
                } else {
                    ekeyPasswordInputEt.text?.clear()
                    ekeyPasswordInputLayout.error = Translation.ekey.insertPasswordLenghtError
                }
            } else confirmPinAndFinish()
        }

        ekeyPasswordInputEt.onTextChanged {
            ekeyPasswordInputLayout.error = null
        }

        ekeyPasswordInputOptionsBtn.setOnClickListener {
            val intent = OverlayActivity.createIntent(
                context, arrayListOf(
                    OverlayButton(type = ButtonType.INPUT_ALPHANUMERIC),
                    OverlayButton(ButtonType.INPUT_NUMERIC)
                )
            )

            startActivityForResult(intent, OverlayActivity.REQUEST_ID)
        }
    }

    private fun confirmPinAndFinish() {
        getEkeyBaseActivity()?.setPin(ekeyPasswordInputEt.text.toString())
        getEkeyBaseActivity()?.refreshClearAndShowMain()
    }

    private fun showKeyboard() {
        handler.postDelayed({
            ekeyPasswordInputEt?.let { v ->
                v.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            }
        }, 200)
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (OverlayActivity.REQUEST_ID == requestCode) {
            val result = data?.getSerializableExtra("res") as? ButtonType?
            when (result) {
                ButtonType.INPUT_NUMERIC -> {
                    ekeyPasswordInputEt.inputType =
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD or InputType.TYPE_CLASS_NUMBER
                }
                ButtonType.INPUT_ALPHANUMERIC -> {
                    ekeyPasswordInputEt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                else -> { /* do nothing */
                }
            }
        }
    }

    companion object {

        private const val ARG_IS_CREATE = "is_create"

        @JvmStatic
        fun newInstance(isCreate: Boolean) =
            EkeyPinComponentFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_CREATE, isCreate)
                }
            }
    }
}