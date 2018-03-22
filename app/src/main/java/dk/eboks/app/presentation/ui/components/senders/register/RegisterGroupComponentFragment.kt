package dk.eboks.app.presentation.ui.components.senders.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_register_component.*

/**
 * Created by Christian on 3/22/2018.
 * @author   Christian
 * @since    3/22/2018.
 */
class RegisterGroupComponentFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_register_component, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val group = arguments.getSerializable(SenderGroup::class.simpleName) as SenderGroup?
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
        }

        registerCancelBtn.setOnClickListener {

        }
    }


    override fun setupTranslations() {

    }
}