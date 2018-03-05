package dk.eboks.app.presentation.ui.components.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import kotlinx.android.synthetic.main.fragment_login_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class LoginComponentFragment : BaseFragment(), LoginComponentContract.View {

    @Inject
    lateinit var presenter : LoginComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_login_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        getBaseActivity()?.setToolbar(R.drawable.ic_red_back, Translation.logoncredentials.title, null, {
            (activity as StartActivity).onBackPressed()
        })
        makeMocks()

    }

    fun makeMocks()
    {
        val li = LayoutInflater.from(context)

        var v = li.inflate(R.layout.viewholder_login_provider, loginProvidersLl, false)
        v.findViewById<ImageView>(R.id.iconIv).setImageResource(R.drawable.ic_fingerprint)
        v.findViewById<TextView>(R.id.nameTv).text = "_Logon with fingerprint"
        v.findViewById<TextView>(R.id.descTv).visibility = View.GONE
        loginProvidersLl.addView(v)

        v = li.inflate(R.layout.viewholder_login_provider, loginProvidersLl, false)
        v.findViewById<ImageView>(R.id.iconIv).setImageResource(R.drawable.ic_idporten)
        v.findViewById<TextView>(R.id.nameTv).text = "_Logon with ID porten"
        v.findViewById<TextView>(R.id.descTv).text = "_Use this to see mail from public authories"
        loginProvidersLl.addView(v)

        v = li.inflate(R.layout.viewholder_login_provider, loginProvidersLl, false)
        v.findViewById<ImageView>(R.id.iconIv).setImageResource(R.drawable.ic_bankid)
        v.findViewById<TextView>(R.id.nameTv).text = "_Logon with BankID"
        v.findViewById<TextView>(R.id.descTv).visibility = View.GONE
        loginProvidersLl.addView(v)
    }

    override fun setupTranslations() {
        headerTv.text = Translation.logoncredentials.topLabel
        detailTv.text = Translation.logoncredentials.topSublabel
        cprEmailTil.hint = Translation.logoncredentials.emailfieldHeader
        passwordTil.hint = Translation.logoncredentials.passwordfieldHeader
    }

}