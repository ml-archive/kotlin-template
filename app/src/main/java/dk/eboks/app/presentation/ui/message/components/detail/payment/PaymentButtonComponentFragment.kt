package dk.eboks.app.presentation.ui.message.components.detail.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.message.payment.Payment
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_payment_button.*
import javax.inject.Inject

class PaymentButtonComponentFragment : BaseFragment(), PaymentButtonComponentContract.View {

    @Inject
    lateinit var presenter: PaymentButtonComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun showPaymentDetails(payment: Payment) {
        paymentButton.text = "${payment.status.title} ${payment.amount ?: ""}"
        paymentDueTv.text = payment.status.text
        paymentButton.setOnClickListener {
            getBaseActivity()?.openComponentDrawer(
                    PaymentComponentFragment::class.java,
                    PaymentComponentFragment.createBundle(payment))
        }
    }

    companion object {

        private const val ARG_PAYMENT = "Payment"

        fun newInstance(payment: Payment) : PaymentButtonComponentFragment {
            val args = Bundle().apply { putParcelable(ARG_PAYMENT, payment) }
            return PaymentButtonComponentFragment().apply { arguments = args }
        }

    }

}