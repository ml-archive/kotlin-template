package dk.eboks.app.presentation.ui.components.start.signup

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.start.welcome.WelcomeComponentFragment
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import kotlinx.android.synthetic.main.fragment_signup_accept_terms_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class AcceptTermsComponentFragment : BaseFragment(), AcceptTermsComponentContract.View {

    @Inject
    lateinit var presenter : AcceptTermsComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_signup_accept_terms_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        acceptBtn.setOnClickListener{getBaseActivity()?.addFragmentOnTop(R.id.containerFl, CompletedComponentFragment(), true) }
        cancelBtn.setOnClickListener{showDialog()}
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

    override fun setupTranslations() {
            headerTv.text = Translation.signup.termsAcceptHeader
            subHeaderTv.text = Translation.signup.termsAcceptSubHeader
            termsContentTv.text = Translation.signup.termsText
            acceptBtn.text = Translation.signup.termsAcceptButton
            cancelBtn.text =Translation.signup.cancelTermsBtn

    }

}