package dk.eboks.app.presentation.ui.components.folder.folders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.folder.folders.newfolder.NewFolderComponentFragment
import dk.eboks.app.util.guard
import dk.eboks.app.util.views
import kotlinx.android.synthetic.main.fragment_folders_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.viewholder_folder.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FoldersComponentFragment : BaseFragment(), FoldersComponentContract.View {

    @Inject
    lateinit var presenter: FoldersComponentContract.Presenter

    var systemfolders: MutableList<Folder> = ArrayList()
    var userfolders: MutableList<Folder> = ArrayList()
    var mode: FolderMode = FolderMode.NORMAL
    var selectFolder: Boolean = false
    var pickedFolder: Folder? = null
    var pickedCheckBox: ImageButton? = null
    var currentUser: User? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_folders_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)

        if(!BuildConfig.ENABLE_FOLDERS_ACTIONS)
        {
            addFolderBtn.visibility = View.GONE
        }

        if (activity.intent.getBooleanExtra("pick", false)) {
            mode = FolderMode.SELECT
        }
        selectFolder = activity.intent.getBooleanExtra("selectFolder", false)

        presenter.onViewCreated(this, lifecycle)


        mainFab.setOnClickListener {
            openDrawer()

        }
        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }
        setupMode()
    }

    private fun animateView() {
        //view should only animate in selectview
        if (mode == FolderMode.SELECT) {
            var animation = AnimationUtils.loadAnimation(context, R.anim.abc_slide_in_bottom)
            animation.duration = 1000
            view?.startAnimation(animation)
        }
    }

    private fun openDrawer() {
        var fragment = NewFolderComponentFragment()
        var arguments = Bundle()
        if (userfolders.size == 0) {
            arguments.putSerializable("disableFolderSelection", true)
        }
        getBaseActivity()?.openComponentDrawer(fragment::class.java, arguments)
    }

    private fun setupMode() {

        refreshSrl.isEnabled = (mode == FolderMode.NORMAL)
        getBaseActivity()?.mainTb?.menu?.clear()
        mainFab.visibility = View.GONE

        systemFoldersLl.alpha = 1f
        systemFoldersLl.isClickable = true

        when (mode) {
            FolderMode.NORMAL -> {
                setNormalTopbar()
                for (view in systemFoldersLl.views) {
                    normalView(view, view.tag as Folder)
                }

                for (view in foldersLl.views) {
                    normalView(view, view.tag as Folder)
                }
                addFolderBtn.setOnClickListener {
                    openDrawer()
                }
            }
            FolderMode.SELECT -> {
                setSelectTopbar()
                showUserFolders(userfolders)
            }
            FolderMode.EDIT -> {
                setEditTopBar()

                for (view in systemFoldersLl.views) {
                    editView(view)
                    systemFoldersLl.alpha = 0.5f
                }

                for (view in foldersLl.views) {
                    editView(view)
                }
                mainFab.visibility = View.VISIBLE
            }

        }
    }

    private fun setNormalTopbar() {
        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.title = Translation.folders.foldersHeader
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            activity.onBackPressed()
        }

        if(BuildConfig.ENABLE_FOLDERS_ACTIONS) {
            val menuProfile = getBaseActivity()?.mainTb?.menu?.add(Translation.folders.topbarEdit)
            menuProfile?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            menuProfile?.setOnMenuItemClickListener { item: MenuItem ->
                mode = FolderMode.EDIT
                setupMode()
                true
            }
        }
    }

    private fun setEditTopBar() {
        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.title = Translation.folders.topbarEdit
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            mode = FolderMode.NORMAL
            setupMode()
        }
    }

    private fun setSelectTopbar() {
        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_close_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            activity.onBackPressed()
        }

        getBaseActivity()?.mainTb?.title = Translation.overlaymenu.chooseLocation

        val menuProfile = getBaseActivity()?.mainTb?.menu?.add(Translation.overlaymenu.done)
        menuProfile?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuProfile?.setOnMenuItemClickListener { item: MenuItem ->
            pickedFolder?.let {
                var intent = Intent()
                it.parentFolder = null
                intent.putExtra("res", it)
                getBaseActivity()?.setResult(Activity.RESULT_OK, intent)
                getBaseActivity()?.finish()
            }.guard {
                getBaseActivity()?.setResult(Activity.RESULT_CANCELED, Intent())
                getBaseActivity()?.finish()
            }
            true

        }
    }

    override fun setUser(user: User?) {
        currentUser = user
    }

    override fun getModeType(): FolderMode {
        return mode
    }

    override fun showSelectFolders(folders: List<Folder>) {
        foldersLl.removeAllViews()
        processFoldersRecursive(folders, 0, null)
    }

    override fun showSystemFolders(folders: List<Folder>) {
        systemfolders.clear()
        systemfolders.addAll(folders)
        systemFoldersLl.removeAllViews()
        val li: LayoutInflater = LayoutInflater.from(context)
        for (folder in folders) {
            var v = li.inflate(R.layout.viewholder_folder, systemFoldersLl, false)
            v.tag = folder
            v.findViewById<TextView>(R.id.nameTv)?.text = folder.name
            if (folder.unreadCount != 0) {
                v.findViewById<TextView>(R.id.badgeCountTv)?.visibility = View.VISIBLE
                v.findViewById<TextView>(R.id.badgeCountTv)?.text = "${folder.unreadCount}"
                v.findViewById<ImageView>(R.id.chevronRightIv)?.visibility = View.GONE
            } else {
                v.findViewById<TextView>(R.id.badgeCountTv)?.visibility = View.GONE
                v.findViewById<ImageView>(R.id.chevronRightIv)?.visibility = View.VISIBLE
            }

            when (mode) {
                FolderMode.EDIT -> {
                    editView(v)
                }
                else -> {
                    //default to normal
                    normalView(v, folder)
                }
            }


            folder.type?.let { type ->
                val iv = v.findViewById<ImageView>(R.id.iconIv)
                iv?.let { it.setImageResource(type.getIconResId()) }
            }

            systemFoldersLl.addView(v)
        }
    }

    override fun showUserFolders(folders: List<Folder>) {
        userfolders.clear()
        userfolders.addAll(folders)
        foldersLl.removeAllViews()
        if (folders.size == 0) {
            userFolderEmptyLl.visibility = View.VISIBLE
            bottomDivider.visibility = View.GONE
        } else {
            userFolderEmptyLl.visibility = View.GONE
            bottomDivider.visibility = View.VISIBLE
            processFoldersRecursive(folders, 0, null)
        }
    }

    fun processFoldersRecursive(folders: List<Folder>, level: Int, parentFolder: Folder? = null) {
        val li: LayoutInflater = LayoutInflater.from(context)
        for (folder in folders) {

            parentFolder?.let {
                var parent = parentFolder
                folder.parentFolder = parent
            }.guard { folder.parentFolder = null }

            val v = li.inflate(R.layout.viewholder_folder, foldersLl, false)
            v.tag = folder
            var left = resources.displayMetrics.density * 40.0f * level.toFloat()
            if (left == 0f)
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

            folder.type?.let { type ->
                val iv = v.findViewById<ImageView>(R.id.iconIv)
                iv?.let { it.setImageResource(type.getIconResId()) }
            }


            // custom
            when (mode) {
                FolderMode.NORMAL -> {
                    normalView(v, folder)
                }

                FolderMode.SELECT -> {
                    selectView(v, folder)
                }

                FolderMode.EDIT -> {
                    editView(v)
                }

            }


            foldersLl.addView(v)


            if (folder.folders.isNotEmpty()) {
                processFoldersRecursive(folder.folders, level + 1, folder)
            } else {
                v.findViewById<View>(R.id.underLineV)?.visibility = View.GONE
            }
        }
    }

    private fun selectView(v: View, folder: Folder) {
        v.findViewById<FrameLayout>(R.id.arrowFl)?.visibility = View.GONE
        v.findViewById<ImageButton>(R.id.editIb)?.visibility = View.GONE
        val checkbox = v.findViewById<ImageButton>(R.id.checkboxIb)
        checkbox?.visibility = View.VISIBLE
        checkbox.setOnClickListener {
            setSelected(checkbox, folder)
        }
        v.setOnClickListener {
            setSelected(checkbox, folder)
        }

        if (selectFolder) {
            if (folder.type == FolderType.INBOX) {
                currentUser?.let { user ->
                    v.nameTv.text = user.name
                    v.iconIv?.let {
                        Glide.with(context)
                                .applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.icon_48_profile_grey))
                                .load(user.avatarUri)
                                .into(it)
                    }
                }
            }
        }
    }

    private fun normalView(v: View, folder: Folder) {
        v.findViewById<FrameLayout>(R.id.arrowFl)?.visibility = View.VISIBLE
        v.findViewById<ImageButton>(R.id.checkboxIb)?.visibility = View.GONE
        v.findViewById<ImageButton>(R.id.editIb)?.visibility = View.GONE
        v.setOnClickListener { presenter.openFolder(folder) }

    }

    private fun editView(v: View) {
        v.findViewById<FrameLayout>(R.id.arrowFl)?.visibility = View.GONE
        v.findViewById<ImageButton>(R.id.checkboxIb)?.visibility = View.GONE
        val edit = v.findViewById<ImageButton>(R.id.editIb)
        var folder = v.tag as Folder

        if (folder.type?.isSystemFolder() == false) {
            edit?.visibility = View.VISIBLE

            edit.setOnClickListener {
                editButtonClicked(v)
            }
            v.setOnClickListener {
                editButtonClicked(v)
            }
        } else {
            v.isClickable = false
        }
    }

    private fun editButtonClicked(v: View) {
        var arguments = Bundle()
        var editFolder = v.tag as Folder
        arguments.putSerializable("editFolder", editFolder)
        getBaseActivity()?.openComponentDrawer(NewFolderComponentFragment::class.java, arguments)
    }


    private fun setSelected(checkbox: ImageButton?, folder: Folder) {
        checkbox?.isSelected?.let { isSelected ->
            if (isSelected) {
                unSelectCurrent()
            } else {
                checkbox?.isSelected = true
                unSelectCurrent()
                pickedFolder = folder
                pickedCheckBox = checkbox
            }
        }
    }

    private fun unSelectCurrent() {
        pickedCheckBox?.let {
            pickedCheckBox?.isSelected = false
        }
    }

    override fun showRefreshProgress(show: Boolean) {
        refreshSrl.isRefreshing = show
        if (!show) {
            animateView()
        }
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showEmpty(show: Boolean) {
    }
}