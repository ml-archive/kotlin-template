package dk.eboks.app.presentation.ui.components.senders.register

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_register_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 3/22/2018.
 * @author   Christian
 * @since    3/22/2018.
 */
class RegisterGroupComponentFragment : BaseFragment(), RegistrationContract.View {


    @Inject
    lateinit var presenter : RegistrationContract.Presenter

    override fun showSuccess() {
        Timber.i("Success!")
    }

    override fun showError(message: String) {
        Timber.i("Fail!")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_register_component, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        val group = arguments.getSerializable(SenderGroup::class.simpleName) as SenderGroup?
        val senderId = arguments.getLong(Sender::class.simpleName, 0)

        if (group != null) {
            registerTitleTv.text = group.name
            registerContentTv.text = group.description?.text
            when (group.registered) {
                0 -> {
                    registerRegBtn.text = Translation.senderdetails.register
                    registerRegBtn.isEnabled = true
                }
                1 -> {
                    registerRegBtn.text = Translation.senderdetails.registeredTypeYes
                    registerRegBtn.isEnabled = false
                }
                else -> ""
            }

            registerAliasContainerLL.removeAllViews()

            val theEditors = ArrayList<TextInputEditText>()
            val theWatcher = object : TextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable) {
                    registerRegBtn.isEnabled = theEditors.none {
                        it.text.toString().trim().isBlank()
                    }
                }
            }

            if (group.alias?.isNotEmpty() == true) {
                registerRegBtn.isEnabled = false // disable until all ET's has been filled

                group.alias?.forEach {
                    val v = inflator.inflate(R.layout.viewholder_textinput, registerAliasContainerLL, false)
                    val input = v.findViewById<TextInputLayout>(R.id.inputTil)
                    val edit = v.findViewById<TextInputEditText>(R.id.inputTiEt)

                    theEditors.add(edit)

                    edit.tag = it
                    edit.addTextChangedListener(theWatcher)

                    input.hint = it.name

                    registerAliasContainerLL.addView(v)
                }
            }
            registerRegBtn.setOnClickListener {
                Timber.i("Register")
                if(theEditors.isNotEmpty()) {
                    val aliases = HashMap<String, String>()
                    for(e in theEditors) {
                        aliases.put(e.tag.toString(), e.text.toString().trim())
                    }
                }

                presenter.registerSenderGroup(senderId, group)
            }
        }

        registerCancelBtn.setOnClickListener {

        }
    }

}