package dk.eboks.app.presentation.ui.mail.folder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Folder
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_folder.*
import kotlinx.android.synthetic.main.include_toolnar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FolderActivity : MainNavigationBaseActivity(), FolderContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }

    @Inject lateinit var presenter: FolderContract.Presenter

    var folders: MutableList<Folder> = ArrayList()

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        /*
        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }
        */
    }

    override fun onResume() {
        super.onResume()
        NStack.translate(this@FolderActivity)
    }

    override fun onShake() {
        if(showEmptyState)
        {
        }
        else
        {
        }
    }

    override fun setupTranslations() {
        toolbarTv.visibility = View.GONE
        toolbarLargeTv.visibility = View.VISIBLE
        toolbarLargeTv.text = "_Folders"

    }

    override fun showError(msg: String) {
        Timber.e(msg)
    }

    override fun showSystemFolders(folders: List<Folder>) {
        systemFoldersLl.removeAllViews()
        val li: LayoutInflater = LayoutInflater.from(this)
        for (folder in folders) {
            var v = li.inflate(R.layout.viewholder_folder, systemFoldersLl, false)
            v.findViewById<TextView>(R.id.nameTv)?.text = folder.name
            if (folder.unreadCount != 0) {
                v.findViewById<TextView>(R.id.badgeCountTv)?.visibility = View.VISIBLE
                v.findViewById<TextView>(R.id.badgeCountTv)?.text = "${folder.unreadCount}"
                v.findViewById<ImageView>(R.id.chevronRightIv)?.visibility = View.GONE
            } else {
                v.findViewById<TextView>(R.id.badgeCountTv)?.visibility = View.GONE
                v.findViewById<ImageView>(R.id.chevronRightIv)?.visibility = View.VISIBLE
            }

            val iv = v.findViewById<ImageView>(R.id.iconIv)
            iv?.let { Glide.with(this@FolderActivity).load(folder.iconImageUrl).into(it) }
            v.setOnClickListener { }
            systemFoldersLl.addView(v)
        }
    }


    override fun showRefreshProgress(show: Boolean) {
        refreshSrl.isRefreshing = show
    }

}
