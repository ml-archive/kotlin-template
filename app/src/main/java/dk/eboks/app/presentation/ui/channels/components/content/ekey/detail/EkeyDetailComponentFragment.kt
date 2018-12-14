package dk.eboks.app.presentation.ui.channels.components.content.ekey.detail

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.ekey.*
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.BaseEkeyFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentActivity
import dk.eboks.app.util.guard
import kotlinx.android.synthetic.main.fragment_channel_ekey_detail.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyDetailComponentFragment : BaseEkeyFragment(), EkeyDetailComponentContract.View {

    var category: EkeyDetailMode? = null
    var editKey: BaseEkey? = null
    var hidePassword: Boolean = false

    @Inject
    lateinit var presenter: EkeyDetailComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_ekey_detail, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        arguments?.let { args ->
            category = args.getSerializable("category") as EkeyDetailMode
            if (args.containsKey("login")) {
                editKey = args.get("login") as Login
            }

            if (args.containsKey("pin")) {
                editKey = args.get("pin") as Pin
            }

            if (args.containsKey("note")) {
                editKey = args.get("note") as Note
            }
        }

        pinShowPasswordIb.setOnClickListener {
            showPassword()
        }

        loginShowPasswordIb.setOnClickListener {
            showPassword()
        }
        showPassword()
        setupTopBar()
        setupInputfields()

        fuckosThief.requestFocus()
    }

    override fun showPinView() {
        val act = if(activity is EkeyContentActivity) {
            activity as EkeyContentActivity
        } else {
            null
        }
        act?.clearBackStackAndSetToPin(false)
    }

    override fun onSuccess() {
        getEkeyBaseActivity()?.refreshAndShowMain()
    }

    private fun setupInputfields() {
        pinShowPasswordIb.visibility = View.GONE
        loginShowPasswordIb.visibility = View.GONE
        pinTil.visibility = View.GONE
        usernameTil.visibility = View.GONE
        passwordTil.visibility = View.GONE

        when (category) {
            EkeyDetailMode.LOGIN -> {
                usernameTil.visibility = View.VISIBLE
                passwordTil.visibility = View.VISIBLE

                editKey?.let {
                    if (it is Login) {
                        nameEt.setText(it.name)
                        usernameEt.setText(it.username)
                        passwordEt.setText(it.password)
                        noteEt.setText(it.note)
                        loginShowPasswordIb.visibility = View.VISIBLE
                    }
                }

                nameEt.requestFocus()
            }
            EkeyDetailMode.PIN -> {
                pinTil.visibility = View.VISIBLE

                editKey?.let {
                    if (it is Pin) {
                        nameEt.setText(it.name)
                        pinEt.setText(it.pin)
                        noteEt.setText(it.note)
                        pinShowPasswordIb.visibility = View.VISIBLE
                    }
                }
                nameEt.requestFocus()
            }
            EkeyDetailMode.NOTE -> {
                editKey?.let {
                    if (it is Note) {
                        nameEt.setText(it.name)
                        noteEt.setText(it.note)
                    }
                }
                nameEt.requestFocus()
            }
        }
    }

    private fun showPassword(){
        hidePassword = !hidePassword
        if (hidePassword) {
            passwordEt.transformationMethod = PasswordTransformationMethod.getInstance()
            pinEt.transformationMethod = PasswordTransformationMethod.getInstance()
            pinShowPasswordIb.isSelected = false
            loginShowPasswordIb.isSelected = false
        } else {
            passwordEt.transformationMethod = null
            pinEt.transformationMethod = null
            pinShowPasswordIb.isSelected = true
            loginShowPasswordIb.isSelected = true

        }
    }

    private fun setupTopBar() {
        getBaseActivity()?.mainTb?.menu?.clear()
        val item = when {
            category == EkeyDetailMode.LOGIN && editKey == null -> {
                Translation.ekey.addLogin
            }
            category == EkeyDetailMode.LOGIN && editKey != null -> {
                Translation.ekey.editLogin
            }
            category == EkeyDetailMode.PIN && editKey == null -> {
                Translation.ekey.addPinCode
            }
            category == EkeyDetailMode.PIN && editKey != null -> {
                Translation.ekey.editPinCode
            }
            category == EkeyDetailMode.NOTE && editKey == null -> {
                Translation.ekey.addNote
            }
            category == EkeyDetailMode.NOTE && editKey != null -> {
                Translation.ekey.editNote
            }
            else -> {
                Translation.ekey.addItemTopBarTitle
            }
        }

        getBaseActivity()?.mainTb?.title = item

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.onBackPressed()
        }

        val menuSearch = getBaseActivity()?.mainTb?.menu?.add(Translation.defaultSection.save.toUpperCase())
        menuSearch?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuSearch?.setOnMenuItemClickListener { item: MenuItem ->

            val items = (activity as EkeyContentActivity).getVault()
            val doesExist = items?.firstOrNull { i -> i.hashCode() == editKey?.hashCode() }

            items?.let {items ->
                doesExist?.let { items.remove(it) }
                if(isDataValid()) {
                    getBaseEkey()?.let { presenter.putVault(items, it) }
                }
            }
            true
        }
    }

    private fun isDataValid(): Boolean {
        var valid = true
        usernameTil.error = null
        passwordTil.error = null
        nameTil.error = null
        noteTil.error = null
        pinTil.error = null
        return when (category) {
            EkeyDetailMode.LOGIN -> {
                if(usernameEt.text.toString().isEmpty()) {
                    usernameTil.error = Translation.ekey.fieldIsRequired
                    valid = false
                }
                if(passwordEt.text.toString().isEmpty()) {
                    passwordTil.error = Translation.ekey.fieldIsRequired
                    valid = false
                }
                if(nameEt.text.toString().isEmpty()) {
                    nameTil.error = Translation.ekey.fieldIsRequired
                    valid = false
                }
                valid
            }
            EkeyDetailMode.PIN -> {
                if(pinEt.text.toString().isEmpty()) {
                    pinTil.error = Translation.ekey.fieldIsRequired
                    valid = false
                }
                if(nameEt.text.toString().isEmpty()) {
                    nameTil.error = Translation.ekey.fieldIsRequired
                    valid = false
                }
                valid
            }
            EkeyDetailMode.NOTE -> {
                if(nameEt.text.toString().isEmpty()) {
                    nameTil.error = Translation.ekey.fieldIsRequired
                    valid = false
                }
                valid
            }
            else -> {
                true
            }
        }
    }

    private fun getBaseEkey(): BaseEkey? {
        return when (category) {
            EkeyDetailMode.LOGIN -> {
                Login(
                        usernameEt.text.toString(),
                        passwordEt.text.toString(),
                        nameEt.text.toString(),
                        noteEt.text.toString())
            }
            EkeyDetailMode.PIN -> {
                Pin(
                        nameEt.text.toString(),
                        pinEt.text.toString(),
                        noteEt.text.toString())
            }
            EkeyDetailMode.NOTE -> {
                Note(nameEt.text.toString(),
                        noteEt.text.toString())
            }
            else -> {
                null
            }
        }
    }

}