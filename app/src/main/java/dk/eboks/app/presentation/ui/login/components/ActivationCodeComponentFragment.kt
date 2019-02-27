package dk.eboks.app.presentation.ui.login.components

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.SheetComponentActivity
import dk.eboks.app.presentation.ui.home.screens.HomeActivity
import dk.eboks.app.presentation.ui.start.screens.HelpActivity
import dk.eboks.app.util.isValidActivationCode
import kotlinx.android.synthetic.main.fragment_activation_code_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ActivationCodeComponentFragment : BaseFragment(), dk.eboks.app.keychain.presentation.components.ActivationCodeComponentContract.View {
    @Inject
    lateinit var presenter: dk.eboks.app.keychain.presentation.components.ActivationCodeComponentContract.Presenter

    var mHandler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activation_code_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        headerTv.requestFocus()

        findActivationCodeBtn.setOnClickListener {
            startActivity(Intent(context, HelpActivity::class.java))
        }
        continueBtn.setOnClickListener {
            val ac = activationCodeEt.text.toString().trim()
            presenter.updateLoginState(ac)
            presenter.login()
        }

        cancelBtn.text = Translation.defaultSection.cancel
        cancelBtn.setOnClickListener {
            (activity as SheetComponentActivity).onBackPressed()
        }
    }

    private fun setupValidation() {
        activationCodeEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(activationCode: Editable?) {
                activationCodeTil.error = null
                mHandler.removeCallbacksAndMessages(null)
                mHandler.postDelayed({
                    if (!continueBtn.isEnabled) {
                        activationCodeTil.error = Translation.activationcode.invalidActivationCode
                    }
                }, 1200)

                continueBtn.isEnabled = activationCode?.isValidActivationCode() ?: false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onResume() {
        super.onResume()
        setupValidation()
    }

    override fun onPause() {
        mHandler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun proceedToApp() {
        startActivity(Intent(context, HomeActivity::class.java))
        activity?.finish()
    }

    override fun showError(error: String?) {
        activationCodeTil.error = error ?: Translation.activationcode.invalidActivationCode
    }

    override fun setDebugUp(activationCode: String?) {
        if (BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) {
            activationCodeEt.setText(activationCode)
            continueBtn.isEnabled = activationCodeEt.text?.isValidActivationCode() ?: false
        }
    }

    override fun showProgress(show: Boolean) {
        buttonGroupLl.visibility = if (show) View.INVISIBLE else View.VISIBLE
        progressFl.visibility = if (!show) View.GONE else View.VISIBLE
    }
}