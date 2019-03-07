package dk.eboks.app.presentation.ui.message.components.detail.payment


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.visible
import kotlinx.android.synthetic.main.fragment_payment_status_component.*


class PaymentStatusComponentFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_status_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        showServiceReceipt()

    }

    private fun setupListeners() {
        paymentCancelButton.setOnClickListener {
            showPaymentCancelConfirmation()
            showCreditCardReceipt(false)
        }
    }

    private fun showPaymentCancelConfirmation() {
        AlertDialog.Builder(context)
                .setTitle("Are you sure you want to cancel the payment?")
                .setMessage("Cras justo odio, dapibus ac facilisis in, egestas eget quam. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit.")
                .setNegativeButton(Translation.defaultSection.no) { d, _ -> d.dismiss()}
                .setPositiveButton(Translation.defaultSection.yes) { d, _ -> d.dismiss()}
                .setOnDismissListener { showServiceReceipt() }
                .show()
    }


    fun showCreditCardReceipt(isCancellable: Boolean) {
        paymentServiceLl.visible = false
        paymentCancelLl.visible = isCancellable

    }

    fun showServiceReceipt() {
        paymentCancelLl.visible = false
        paymentServiceLl.visible = true
        dividerTransaction.visible = false
        paymentTransactionLl.visible = false

    }


}
