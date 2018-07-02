package dk.eboks.app.presentation.ui.components.mail.foldershortcuts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderActivity
import kotlinx.android.synthetic.main.fragment_folder_shortcuts_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FolderShortcutsComponentFragment : BaseFragment(), FolderShortcutsComponentContract.View {

    @Inject
    lateinit var presenter : FolderShortcutsComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_folder_shortcuts_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

    }

    override fun showFolders(folders: List<Folder>) {
        yourMailLl.removeAllViews()
        val li : LayoutInflater = LayoutInflater.from(context)
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

            folder.type?.let { type->
                val iv = v.findViewById<ImageView>(R.id.iconIv)
                iv?.let { it.setImageResource(type.getIconResId()) }
            }

            v.setOnClickListener { presenter.openFolder(folder) }
            yourMailLl.addView(v)
        }
        insertTestData()
    }

    fun insertTestData()
    {
        val li : LayoutInflater = LayoutInflater.from(context)
        var v = li.inflate(R.layout.viewholder_folder, yourMailLl, false)
        v.findViewById<TextView>(R.id.nameTv)?.text = Translation.folders.foldersHeader
        v.findViewById<TextView>(R.id.badgeCountTv)?.text = "2"

        val iv = v.findViewById<ImageView>(R.id.iconIv)
        iv?.setImageResource(R.drawable.ic_folder)
        v.setOnClickListener {
            startActivity(Intent(context, FolderActivity::class.java))
        }
        yourMailLl.addView(v)
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if(show) View.VISIBLE else View.GONE
    }

    override fun showEmpty(show: Boolean) {

    }
}