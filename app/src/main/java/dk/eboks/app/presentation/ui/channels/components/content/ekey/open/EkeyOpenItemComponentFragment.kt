package dk.eboks.app.presentation.ui.channels.components.content.ekey.open

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.ekey.*
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailMode
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentActivity
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.fragment_channel_ekey_openitem.*
import kotlinx.android.synthetic.main.include_toolbar.*
import java.lang.StringBuilder
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyOpenItemComponentFragment : BaseFragment(), EkeyOpenItemComponentContract.View {

    var ekey: BaseEkey? = null
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

            if (args.containsKey("ekey")) {
                ekey = args.get("ekey") as Ekey
            }
        }

        setupTopbar()
        setup()
    }

    private fun setupTopbar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        when (ekey) {
            is Login -> {
                getBaseActivity()?.mainTb?.title = Translation.ekey.addItemLogin
            }
            is Pin -> {
                getBaseActivity()?.mainTb?.title = Translation.ekey.addItemPinCode
            }
            is Note -> {
                getBaseActivity()?.mainTb?.title = Translation.ekey.addItemNote
            }
            is Ekey -> {
                getBaseActivity()?.mainTb?.title = Translation.ekey.overviewEkey
            }
        }

        val categoryString = when(ekey) {
            is Login -> {
                Translation.ekey.deleteItemDescriptionLogin
            }
            is Pin -> {
                Translation.ekey.deleteItemDescriptionPinCode
            }
            is Note -> {
                Translation.ekey.deleteItemDescriptionNote
            }
            else -> {
                Translation.ekey.deleteItemDescriptionEKey
            }
        }

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.onBackPressed()
        }

        if(ekey !is Ekey) {
            val menuSearch = getBaseActivity()?.mainTb?.menu?.add("_settings")
            menuSearch?.setIcon(R.drawable.icon_48_delete_red)
            menuSearch?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            menuSearch?.setOnMenuItemClickListener { item: MenuItem ->

                AlertDialog.Builder(context)
                        .setMessage(Translation.ekey.deleteDialogMsg.replace("[item]", categoryString))
                        .setPositiveButton(Translation.ekey.deleteDialogRemoveBtn) { dialog, which ->
                            ekey?.let {
                                val items = (activity as EkeyContentActivity).getVault()
                                items?.removeAll { r -> r.hashCode() == it.hashCode() }
                                items?.let { items -> presenter.putVault(items) }
                            }
                        }
                        .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->

                        }
                        .show()
                true
            }
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
                        var clip = android.content.ClipData.newPlainText("copied password", it.password)
                        clipboard?.primaryClip = clip
                        Toast.makeText(context, (Translation.ekey.copiedToClipboard), Toast.LENGTH_SHORT).show()
                }
                    copyUsernameBtn.setOnClickListener { view ->
                        var clipboard  = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        var clip = android.content.ClipData.newPlainText("copied username", it.username)
                        clipboard?.primaryClip = clip
                        Toast.makeText(context, (Translation.ekey.copiedToClipboard), Toast.LENGTH_SHORT).show()
                    }
                }
                is Pin -> {
                    pinContainerFl.visibility = View.VISIBLE
                    cardholderHintTv.visibility = View.GONE
                    cardholderTv.visibility = View.GONE
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
                is Ekey -> {
                    pinContainerFl.visibility = View.VISIBLE
                    pinTv.text = getPassword(it.pin)
                    pinShowPasswordIb.isSelected = !hidePassword
                    pinShowPasswordIb.setOnClickListener {
                        hidePassword = !hidePassword
                        pinShowPasswordIb.isSelected = !hidePassword
                        ekey?.let {
                            it as Ekey
                            pinTv.text = getPassword(it.pin)
                        }
                    }
                    editBtn.visibility = View.GONE
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
                    is Ekey -> {
                        fragment.putArg("ekey", key)
                    }
                }
                getBaseActivity()?.addFragmentOnTop(R.id.content, fragment, true)
            }
        }
    }

    override fun onSuccess() {
        (activity as EkeyContentActivity).shouldRefresh = true
        getBaseActivity()?.setRootFragment(R.id.content, EkeyComponentFragment.newInstance())
    }

    override fun showPinView() {
        val act = if(activity is EkeyContentActivity) {
            activity as EkeyContentActivity
        } else {
            null
        }
        act?.clearBackStackAndSetToPin()
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