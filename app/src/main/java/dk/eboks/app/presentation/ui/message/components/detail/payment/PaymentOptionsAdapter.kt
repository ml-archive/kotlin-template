package dk.eboks.app.presentation.ui.message.components.detail.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.message.PaymentOption
import kotlinx.android.synthetic.main.viewholder_payment_option.view.*

class PaymentOptionsAdapter(val options: List<PaymentOption>, val listener: PaymentOptionListener) : RecyclerView.Adapter<PaymentOptionsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_payment_option, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = options.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(options[position])
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(paymentOption: PaymentOption) {
            view.paymentOptionIv.setImageResource(R.drawable.icon_48_alternative_bs_white)
            view.paymentOptionTv.text = paymentOption.name
            view.setOnClickListener { listener.onPaymentOptionSelected(paymentOption) }
        }
    }

    interface PaymentOptionListener {
        fun onPaymentOptionSelected(paymentOption: PaymentOption)
    }

}