package dk.eboks.app.presentation.ui.channels.components.content.ekey.pin

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentFragment
import kotlinx.android.synthetic.main.fragment_channel_ekey_pin.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyPinComponentFragment : BaseFragment(), EkeyPinComponentContract.View {

    @Inject
    lateinit var presenter: EkeyPinComponentContract.Presenter

    var handler = Handler()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_ekey_pin, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupInputfields()
        setupTopbar()

        //todo will neeed logic to determine if its loggin in or creating a user


    }

    private fun setupTopbar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.title = "_login"

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.finish()
        }

    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    private fun setupInputfields() {

        showKeyboard()


        thief.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (s.length > 0) {
                        pin1Et.setText(s[0].toString())
                    } else {
                        pin1Et.setText("")
                    }
                    if (s.length > 1) {
                        pin2Et.setText(s[1].toString())
                    }else {
                        pin2Et.setText("")
                    }
                    if (s.length > 2) {
                        pin3Et.setText(s[2].toString())
                    }else {
                        pin3Et.setText("")
                    }
                    if (s.length > 3) {
                        pin4Et.setText(s[3].toString())

                        //todo try to login
                        getBaseActivity()?.addFragmentOnTop(R.id.content,EkeyComponentFragment.newInstance(s.toString()))
                    }else {
                        pin4Et.setText("")
                    }
                }


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun showKeyboard() {
        handler.postDelayed({
            thief.requestFocus()
            var imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(thief, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }
}