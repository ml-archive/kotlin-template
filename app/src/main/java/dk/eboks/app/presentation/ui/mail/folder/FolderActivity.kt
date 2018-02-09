package dk.eboks.app.presentation.ui.mail.folder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.eboks.app.presentation.ui.mail.list.MailListActivity
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
    var indentationLevel = 0

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }

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
        toolbarLargeTv.text = Translation.folders.foldersHeader

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
            iv?.let { it.setImageResource(folder.type.getIconResId()) }
            v.setOnClickListener { openFolder(folder) }
            systemFoldersLl.addView(v)
        }
    }

    override fun showUserFolders(folders: List<Folder>) {
        foldersLl.removeAllViews()
        indentationLevel = 0
        processFoldersRecursive(folders)
    }

    fun processFoldersRecursive(folders: List<Folder>)
    {
        val li: LayoutInflater = LayoutInflater.from(this)
        for(folder in folders)
        {
            Timber.e("$indentationLevel: ${folder.name}")


            val v = li.inflate(R.layout.viewholder_folder, foldersLl, false)
            var left  = resources.displayMetrics.density * 40.0f * indentationLevel.toFloat()
            if(left == 0f)
                left = resources.displayMetrics.density * 16f
            v.setPadding(left.toInt(), v.paddingTop, v.paddingRight, v.paddingBottom)
            v.findViewById<View>(R.id.underLineV)?.visibility = View.VISIBLE
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
            iv?.let { it.setImageResource(folder.type.getIconResId()) }
            v.setOnClickListener { openFolder(folder) }
            foldersLl.addView(v)


            if(folder.folders.isNotEmpty()) {
                indentationLevel++
                processFoldersRecursive(folder.folders)
            }
        }
    }

    fun openFolder(folder: Folder) {
        presenter.setCurrentFolder(folder)
        startActivity(Intent(this, MailListActivity::class.java))
        //overridePendingTransition(0, 0)
    }

    override fun showRefreshProgress(show: Boolean) {
        refreshSrl.isRefreshing = show
    }

}
