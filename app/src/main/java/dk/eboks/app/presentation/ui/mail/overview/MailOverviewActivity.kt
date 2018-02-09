package dk.eboks.app.presentation.ui.mail.overview

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.models.FolderType
import dk.eboks.app.domain.models.Sender
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.eboks.app.presentation.ui.dialogs.ConfirmDialogFragment
import dk.eboks.app.presentation.ui.dialogs.ContextSheetActivity
import dk.eboks.app.presentation.ui.mail.folder.FolderActivity
import dk.eboks.app.presentation.ui.mail.list.MailListActivity
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

    var senders : MutableList<Sender> = ArrayList()

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_overview)
        setupRecyclerView()
        //setupYourMail()

        yourMailTv.setOnClickListener {
            showConfirmDialog()
        }

        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }

        userShareTv.setOnClickListener {
            startActivity(Intent(this@MailOverviewActivity, ContextSheetActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        userShareTv.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        NStack.translate(this@MailOverviewActivity)
    }

    override fun onShake() {
        if(showEmptyState)
        {
            sendersListEmptyLl.visibility = View.VISIBLE
            sendersListLl.visibility = View.GONE
        }
        else
        {
            sendersListLl.visibility = View.VISIBLE
            sendersListEmptyLl.visibility = View.GONE
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


    override fun showError(msg: String) {
        Log.e("debug", msg)
    }

    override fun showSenders(senders: List<Sender>) {
        this.senders.addAll(senders)
        sendersRv.adapter.notifyDataSetChanged()
    }

    override fun showFolders(folders: List<Folder>) {
        yourMailLl.removeAllViews()
        val li : LayoutInflater = LayoutInflater.from(this)
        insertTestData()
        for(folder in folders)
        {
            var v = li.inflate(R.layout.viewholder_folder, yourMailLl, false)
            v.findViewById<TextView>(R.id.nameTv)?.text = folder.name
            if(folder.unreadCount != 0) {
                v.findViewById<TextView>(R.id.badgeCountTv)?.visibility = View.VISIBLE
                v.findViewById<TextView>(R.id.badgeCountTv)?.text = "${folder.unreadCount}"
                v.findViewById<ImageView>(R.id.chevronRightIv)?.visibility = View.GONE
            }
            else
            {
                v.findViewById<TextView>(R.id.badgeCountTv)?.visibility = View.GONE
                v.findViewById<ImageView>(R.id.chevronRightIv)?.visibility = View.VISIBLE
            }

            val iv = v.findViewById<ImageView>(R.id.iconIv)
            iv?.let { it.setImageResource(folder.type.getIconResId()) }
            v.setOnClickListener { openFolder(folder) }
            yourMailLl.addView(v)
        }
    }

    fun insertTestData()
    {
        val li : LayoutInflater = LayoutInflater.from(this)
        var v = li.inflate(R.layout.viewholder_folder, yourMailLl, false)
        v.findViewById<TextView>(R.id.nameTv)?.text = "Folders"
        v.findViewById<TextView>(R.id.badgeCountTv)?.text = "2"

        val iv = v.findViewById<ImageView>(R.id.iconIv)
        iv?.setImageResource(R.drawable.ic_folder)
        v.setOnClickListener {
            startActivity(Intent(this@MailOverviewActivity, FolderActivity::class.java))
        }
        yourMailLl.addView(v)
    }

    override fun showRefreshProgress(show: Boolean) {
        //if(refreshSrl.isRefreshing != show)
            refreshSrl.isRefreshing = show
    }

    override fun openFolder(folder: Folder) {
        startActivity(Intent(this, MailListActivity::class.java))
        //overridePendingTransition(0, 0)
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
            return senders.size
        }

        override fun onBindViewHolder(holder: CircularSenderViewHolder?, position: Int) {
            holder?.circleIv?.let {
                Glide.with(this@MailOverviewActivity).load(senders[position].imageUrl).into(it)
                it.isSelected = senders[position].unreadEmailsCount > 0
            }


        }
    }
}
