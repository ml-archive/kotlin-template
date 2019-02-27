package dk.eboks.app.presentation.ui.login.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_forgot_password_mail_sent_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ForgotPasswordDoneComponentFragment : BaseFragment(),
    dk.eboks.app.keychain.presentation.components.ForgotPasswordDoneComponentContract.View {

    @Inject
    lateinit var presenter: dk.eboks.app.keychain.presentation.components.ForgotPasswordDoneComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_forgot_password_mail_sent_component,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        okBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}