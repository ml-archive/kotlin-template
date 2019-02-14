package dk.eboks.app.presentation.ui.message.components.detail.payment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_payment_component.*


class PaymentComponentFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_component, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentCreditCardTv.text = Translation.paymentdrawer.payWithPrefix
        paymentServiceTv.text = Translation.paymentdrawer.registerAndPayWithPrefix
        paymentNotificationSwitch.text = Translation.paymentdrawer.notificationsHeader
        setupListeners()

    }


    private fun setupListeners() {
        paymentCardCL.setOnClickListener {

        }

        paymentServiceLL.setOnClickListener {

        }
    }

}
