package dk.eboks.app.presentation.ui.start.components.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import dk.eboks.app.keychain.presentation.components.SignupComponentContract
import dk.eboks.app.R
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.start.components.welcome.WelcomeComponentFragment
import kotlinx.android.synthetic.main.fragment_signup_accept_terms_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class AcceptTermsComponentFragment : BaseFragment(), SignupComponentContract.TermsView {

    @Inject lateinit var presenter: SignupComponentContract.Presenter

    @Inject lateinit var appConfig: AppConfig

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup_accept_terms_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        arguments?.let { args ->
            if (args.containsKey("justShow")) {
                termsHeaderLl.visibility = View.GONE
                termsButtonsLl.visibility = View.GONE
                toolbarInclude.visibility = View.VISIBLE
                mainTb.title = Translation.signup.termsTitle
                mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
                mainTb.setNavigationOnClickListener {
                    fragmentManager?.popBackStack()
                }
            }
        }

        acceptBtn.setOnClickListener {
            presenter.createUser()
        }

        cancelBtn.setOnClickListener { showDialog() }
        termsWV.loadUrl(appConfig.getTermsAndConditionsUrl())
    }

    private fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(context ?: return)

        dialogBuilder.setTitle(Translation.signup.cancelDialogHeader)
        dialogBuilder.setMessage(Translation.signup.cancelDialogText)
        dialogBuilder.setPositiveButton(Translation.signup.cancelDialogCancelBtn) { dialog, whichButton ->
        }

        dialogBuilder.setNegativeButton(Translation.signup.cancelDialogDiscardBtn) { dialog, whichButton ->
            getBaseActivity()?.addFragmentOnTop(R.id.containerFl, WelcomeComponentFragment(), true)
        }
        val b = dialogBuilder.create()
        b.show()
    }

    override fun showSignupCompleted() {
        getBaseActivity()?.addFragmentOnTop(R.id.containerFl, CompletedComponentFragment(), true)
    }

    override fun showProgress(show: Boolean) {
        termsButtonsLl.visibility = if (show) View.INVISIBLE else View.VISIBLE
        progressFl.visibility = if (!show) View.INVISIBLE else View.VISIBLE
    }
}