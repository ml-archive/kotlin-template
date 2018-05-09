package dk.eboks.app.presentation.ui.components.channels.content.ekey.open

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_channel_ekey_openitem.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyOpenItemComponentFragment : BaseFragment(), EkeyOpenItemComponentContract.View {

    var ekey: Ekey? = null
    @Inject
    lateinit var presenter: EkeyOpenItemComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_ekey_openitem, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        arguments?.let { args ->
            if (args.containsKey("login")) {
                ekey = args.get("login") as Login
            }

            if (args.containsKey("pin")) {
                ekey = args.get("pin") as Pin
            }

            if (args.containsKey("note")) {
                ekey = args.get("note") as Note
            }
        }

        setup()
    }

    private fun setup() {
        ekey?.let {
            nameTv.text = it.name
            noteTv.text = it.note

            when (it) {
                it as Login -> {
                    loginContainerLl.visibility = View.VISIBLE
                    usernameTv.text = it.username
                    passwordTv.text = it.password
                    passwordShowPasswordIb.setOnClickListener {
                        //todo show/hide password
                    }

                }
                it as Pin -> {
                    pinContainerFl.visibility = View.VISIBLE
                }
                it as Note -> {
                }
            }
        }
    }

}