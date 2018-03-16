package dk.eboks.app.presentation.ui.components.start.login

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.SheetComponentActivity
import dk.eboks.app.util.isValidActivationCode
import kotlinx.android.synthetic.main.fragment_activation_code_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ActivationCodeComponentFragment : BaseFragment(), ActivationCodeComponentContract.View {

    @Inject
    lateinit var presenter: ActivationCodeComponentContract.Presenter

    var mHandler = Handler()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_activation_code_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        headerTv.requestFocus()
        cancelBtn.setOnClickListener {
            (activity as SheetComponentActivity).onBackPressed()
        }

        activationCodeEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(activationCode: Editable?) {
                activationCodeTil.error = null
                mHandler.removeCallbacksAndMessages(null)
                continueBtn.isEnabled = activationCode?.isValidActivationCode() ?: false
                mHandler?.postDelayed({
                    if(!continueBtn.isEnabled){
                    activationCodeTil.error = Translation.activationcode.invalidActivationCode
                }
                }, 1200)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun setupTranslations() {
        cancelBtn.text = Translation.defaultSection.cancel
    }

    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}