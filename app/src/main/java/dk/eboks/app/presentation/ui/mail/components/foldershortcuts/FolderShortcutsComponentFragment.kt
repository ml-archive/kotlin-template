package dk.eboks.app.presentation.ui.mail.components.foldershortcuts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.getIconResId
import dk.eboks.app.mail.presentation.ui.components.foldershortcuts.FolderShortcutsComponentContract
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.folder.screens.FolderActivity
import kotlinx.android.synthetic.main.fragment_folder_shortcuts_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FolderShortcutsComponentFragment : BaseFragment(), FolderShortcutsComponentContract.View {

    @Inject
    lateinit var presenter: FolderShortcutsComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_folder_shortcuts_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun showFolders(folders: List<Folder>) {
        yourMailLl.removeAllViews()
        val li: LayoutInflater = LayoutInflater.from(context)
        Timber.d("Filders: $folders")
        for (folder in folders) {
            val v = li.inflate(R.layout.viewholder_folder, yourMailLl, false)
            v.findViewById<TextView>(R.id.nameTv)?.text = folder.name
            if (folder.unreadCount != 0) {
                v.findViewById<TextView>(R.id.badgeCountTv)?.visibility = View.VISIBLE
                v.findViewById<TextView>(R.id.badgeCountTv)?.text = "${folder.unreadCount}"
                v.findViewById<ImageView>(R.id.chevronRightIv)?.visibility = View.GONE
            } else {
                v.findViewById<TextView>(R.id.badgeCountTv)?.visibility = View.GONE
                v.findViewById<ImageView>(R.id.chevronRightIv)?.visibility = View.VISIBLE
            }

            folder.type?.let { type ->
                val iv = v.findViewById<ImageView>(R.id.iconIv)
                iv?.setImageResource(type.getIconResId())
            }

            v.setOnClickListener { presenter.openFolder(folder) }
            yourMailLl.addView(v)
        }
        insertTestData()
    }

    private fun insertTestData() {
        val li: LayoutInflater = LayoutInflater.from(context)
        val v = li.inflate(R.layout.viewholder_folder, yourMailLl, false)
        v.findViewById<TextView>(R.id.nameTv)?.text = Translation.folders.foldersHeader
        v.findViewById<TextView>(R.id.badgeCountTv)?.text = "2"

        val iv = v.findViewById<ImageView>(R.id.iconIv)
        iv?.setImageResource(R.drawable.ic_folder)
        v.setOnClickListener {
            FolderActivity.startAsIntent(context, false)
        }
        yourMailLl.addView(v)
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showEmpty(show: Boolean) {
    }
}