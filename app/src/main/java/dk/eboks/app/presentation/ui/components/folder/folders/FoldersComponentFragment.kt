package dk.eboks.app.presentation.ui.components.folder.folders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Folder
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.mail.list.MailListActivity
import kotlinx.android.synthetic.main.fragment_folders_component.*
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FoldersComponentFragment : BaseFragment(), FoldersComponentContract.View {

    @Inject
    lateinit var presenter : FoldersComponentContract.Presenter

    var folders: MutableList<Folder> = ArrayList()
    var indentationLevel = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_folders_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun setupTranslations() {

    }

    override fun showError(msg: String) {
        Timber.e(msg)
    }

    override fun showSystemFolders(folders: List<Folder>) {
        systemFoldersLl.removeAllViews()
        val li: LayoutInflater = LayoutInflater.from(context)
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
        val li: LayoutInflater = LayoutInflater.from(context)
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
        startActivity(Intent(context, MailListActivity::class.java))
        //overridePendingTransition(0, 0)
    }

    override fun showRefreshProgress(show: Boolean) {
        refreshSrl.isRefreshing = show
    }

}