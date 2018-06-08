package dk.eboks.app.presentation.ui.components.start.signup

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.start.welcome.WelcomeComponentFragment
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import kotlinx.android.synthetic.main.fragment_signup_accept_terms_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class AcceptTermsComponentFragment : BaseFragment(), SignupComponentContract.TermsView {

    @Inject
    lateinit var presenter: SignupComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_signup_accept_terms_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
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
                    fragmentManager.popBackStack()
                }
            }
        }

        acceptBtn.setOnClickListener {
            presenter.createUser()

        }

        cancelBtn.setOnClickListener { showDialog() }
        // TODO fix me
        termsWV.loadUrl("http://test401-mobile-api-dk.e-boks.com/2/resources/${Config.currentMode.countryCode}/terms")
    }

    private fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle(Translation.signup.cancelDialogHeader)
        dialogBuilder.setMessage(Translation.signup.cancelDialogText)
        dialogBuilder.setPositiveButton(Translation.signup.cancelDialogCancelBtn, DialogInterface.OnClickListener { dialog, whichButton ->

        })

        dialogBuilder.setNegativeButton(Translation.signup.cancelDialogDiscardBtn, DialogInterface.OnClickListener { dialog, whichButton ->
            getBaseActivity()?.addFragmentOnTop(R.id.containerFl, WelcomeComponentFragment(), true)
        })
        val b = dialogBuilder.create()
        b.show()
    }

    override fun showUserCreated() {
        getBaseActivity()?.addFragmentOnTop(R.id.containerFl, CompletedComponentFragment(), true)
    }

    override fun showUserCreatedError() {

    }

    override fun showError() {
    }

    override fun showProgress(show: Boolean) {
    }
}