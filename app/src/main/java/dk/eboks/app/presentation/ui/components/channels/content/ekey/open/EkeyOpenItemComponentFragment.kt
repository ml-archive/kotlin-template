package dk.eboks.app.presentation.ui.components.channels.content.ekey.open

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.channels.content.ekey.EkeyItem
import dk.eboks.app.presentation.ui.components.channels.content.ekey.additem.EkeyAddItemComponentFragment
import dk.eboks.app.presentation.ui.components.channels.content.ekey.detail.EkeyDetailComponentFragment
import dk.eboks.app.presentation.ui.components.channels.content.ekey.detail.EkeyDetailMode
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.fragment_channel_ekey_openitem.*
import kotlinx.android.synthetic.main.include_toolbar.*
import java.lang.StringBuilder
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyOpenItemComponentFragment : BaseFragment(), EkeyOpenItemComponentContract.View {

    var ekey: Ekey? = null
    var hidePassword: Boolean = true
    var category = EkeyDetailMode.LOGIN

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

        setupTopbar()
        setup()
    }

    private fun setupTopbar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        when (ekey) {
            is Login -> {
                getBaseActivity()?.mainTb?.title = "_Login"
            }
            is Pin -> {
                getBaseActivity()?.mainTb?.title = "_Pin code"
            }
            is Note -> {
                getBaseActivity()?.mainTb?.title = "_Note"
            }
        }

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.onBackPressed()
        }

        val menuSearch = getBaseActivity()?.mainTb?.menu?.add("_settings")
        menuSearch?.setIcon(R.drawable.icon_48_delete_red)
        menuSearch?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuSearch?.setOnMenuItemClickListener { item: MenuItem ->

            //todo show dialog to delete
            getBaseActivity()?.openComponentDrawer(ChannelSettingsComponentFragment::class.java)
            true
        }
    }

    private fun setup() {
        ekey?.let {
            nameTv.text = it.name

            if(it.note != null) {
                noteTv.text = it.note
            } else {
                noteHeaderTv.visibility = View.GONE
                noteTv.visibility = View.GONE
            }
            when (it) {
                is Login -> {
                    loginContainerLl.visibility = View.VISIBLE
                    usernameTv.text = it.username
                    passwordTv.text = getPassword(it.password)
                    passwordShowPasswordIb.isSelected = !hidePassword
                    category = EkeyDetailMode.LOGIN
                    passwordShowPasswordIb.setOnClickListener {
                        hidePassword = !hidePassword
                        passwordShowPasswordIb.isSelected = !hidePassword
                        ekey?.let {
                            it as Login
                            passwordTv.text = getPassword(it.password)
                        }
                    }

                    copyPasswordBtn.setOnClickListener { view ->
                        var clipboard  = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        var clip = android.content.ClipData.newPlainText("_copied password text", it.password)
                        clipboard?.primaryClip = clip
                    }
                    copyUsernameBtn.setOnClickListener { view ->
                        var clipboard  = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        var clip = android.content.ClipData.newPlainText("_copied username text", it.username)
                        clipboard?.primaryClip = clip
                    }
                }
                is Pin -> {
                    pinContainerFl.visibility = View.VISIBLE
                    pinTv.text = getPassword(it.pin)
                    pinShowPasswordIb.isSelected = !hidePassword
                    category = EkeyDetailMode.PIN
                    pinShowPasswordIb.setOnClickListener {
                        hidePassword = !hidePassword
                        pinShowPasswordIb.isSelected = !hidePassword
                        ekey?.let {
                            it as Pin
                            pinTv.text = getPassword(it.pin)
                        }
                    }

                }
                is Note -> {
                    category = EkeyDetailMode.NOTE
                }
            }
            editBtn.setOnClickListener {
                val fragment = EkeyDetailComponentFragment()

                fragment.putArg("category",category)
                var key = ekey
                when (key) {
                    is Login -> {
                        fragment.putArg("login", key)
                    }
                    is Pin -> {
                        fragment.putArg("pin", key)
                    }
                    is Note -> {
                        fragment.putArg("note", key)
                    }
                }
                getBaseActivity()?.addFragmentOnTop(R.id.content, fragment, true)
            }
        }
    }

    private fun getPassword(password: String): String? {
        if (hidePassword) {
            var sb = StringBuilder()
            for (x in 1..password.length) {
                sb.append('*')
            }
            return sb.toString()
        } else {
            return password
        }
    }


}