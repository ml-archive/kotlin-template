package dk.eboks.app.presentation.ui.mail

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.eboks.app.presentation.ui.dialogs.ConfirmDialogFragment
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_mail_overview.*
import kotlinx.android.synthetic.main.include_toolnar.*
import timber.log.Timber
import javax.inject.Inject

class MailOverviewActivity : MainNavigationBaseActivity(), MailOverviewContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }
    @Inject lateinit var presenter: MailOverviewContract.Presenter

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_overview)
        setupRecyclerView()

        yourMailTv.setOnClickListener {
            showConfirmDialog()
        }
    }

    override fun setupTranslations() {
        sendersHeaderTv.text = Translation.mail.senderHeader
        sendersShowAllTv.text = Translation.mail.showAllSendersButton
        yourMailTv.text = Translation.mail.smartFolderHeader
        sendersEmptyHeaderTv.text = Translation.mail.sendersEmptyHeader
        sendersEmptyDescTv.text = Translation.mail.sendersEmptyMessage
        addMoreSendersBtn.text = Translation.mail.addMoreSendersButton
    }

    fun setupRecyclerView()
    {
        sendersRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        sendersRv.adapter = HorizontalSendersAdapter()
    }

    fun showConfirmDialog()
    {
        Timber.e("Showing confirm dialog")
        var dialog = ConfirmDialogFragment()
        dialog.show(supportFragmentManager, ConfirmDialogFragment::class.simpleName)
    }

    override fun onResume() {
        super.onResume()
        NStack.translate(this@MailOverviewActivity)
    }

    override fun showError(msg: String) {
        Log.e("debug", msg)
    }


    inner class HorizontalSendersAdapter : RecyclerView.Adapter<HorizontalSendersAdapter.CircularSenderViewHolder>() {

        inner class CircularSenderViewHolder(root : View) : RecyclerView.ViewHolder(root)
        {
            val circleIv = root.findViewById<ImageView>(R.id.circleIv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularSenderViewHolder {
            val v = LayoutInflater.from(this@MailOverviewActivity).inflate(R.layout.viewholder_circular_sender, parent, false)
            val vh = CircularSenderViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: CircularSenderViewHolder?, position: Int) {
            holder?.circleIv?.setOnClickListener { view ->
                view.isSelected = !view.isSelected
            }
        }
    }
}
