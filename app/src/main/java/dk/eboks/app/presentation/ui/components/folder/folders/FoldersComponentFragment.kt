package dk.eboks.app.presentation.ui.components.folder.folders

import android.app.Activity
import android.content.Intent
import android.media.Image
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
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.guard
import kotlinx.android.synthetic.main.abc_list_menu_item_checkbox.view.*
import kotlinx.android.synthetic.main.fragment_folders_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FoldersComponentFragment : BaseFragment(), FoldersComponentContract.View {

    @Inject
    lateinit var presenter: FoldersComponentContract.Presenter

    var folders: MutableList<Folder> = ArrayList()
    var pickerMode: Boolean = false
    var pickedFolder: Folder? = null
    var pickedCheckBox: ImageButton? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_folders_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        arguments?.let { args ->
            if (args.containsKey("pick")) { pickerMode = true}

        }

        presenter.onViewCreated(this, lifecycle)

        refreshSrl.isEnabled = !pickerMode
        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }
            setupTopbar()
    }

    private fun animateView (){
        //view should only animate in pickerview
        if (pickerMode){
            var animation = AnimationUtils.loadAnimation(context,R.anim.abc_slide_in_bottom)
            animation.duration = 1000
            view?.startAnimation(animation)
        }
    }

    private fun setupTopbar() {
        if (pickerMode) {
            getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_close_red_navigationbar)
            getBaseActivity()?.mainTb?.setNavigationOnClickListener {
                activity.onBackPressed()
            }

            getBaseActivity()?.mainTb?.title = Translation.overlaymenu.chooseLocation

            val menuProfile = getBaseActivity()?.mainTb?.menu?.add(Translation.overlaymenu.done)
            menuProfile?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            menuProfile?.setOnMenuItemClickListener { item: MenuItem ->
                pickedFolder?.let{
                var intent = Intent()
                intent.putExtra("res", it)
                getBaseActivity()?.setResult(Activity.RESULT_OK, intent)
                getBaseActivity()?.finish()
                }.guard {
                    getBaseActivity()?.setResult(Activity.RESULT_CANCELED, Intent())
                    getBaseActivity()?.finish()
                }
                true

            }
        } else {
            getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
            getBaseActivity()?.mainTb?.title = Translation.folders.foldersHeader
            getBaseActivity()?.mainTb?.setNavigationOnClickListener {
                activity.onBackPressed()
            }
        }
    }

    override fun isEditMode(): Boolean {
        return pickerMode
    }

    override fun showSelectFolders(folders: List<Folder>) {
        foldersLl.removeAllViews()
        processFoldersRecursive(folders, 0)
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
            v.setOnClickListener { presenter.openFolder(folder) }
            systemFoldersLl.addView(v)
        }
    }

    override fun showUserFolders(folders: List<Folder>) {
        foldersLl.removeAllViews()
        processFoldersRecursive(folders, 0)
    }

    fun processFoldersRecursive(folders: List<Folder>, level: Int) {
        val li: LayoutInflater = LayoutInflater.from(context)
        for (folder in folders) {
            Timber.e("$level: ${folder.name}")

            val v = li.inflate(R.layout.viewholder_folder, foldersLl, false)
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

            val iv = v.findViewById<ImageView>(R.id.iconIv)
            iv?.let { it.setImageResource(folder.type.getIconResId()) }

            if (pickerMode) {
                v.findViewById<FrameLayout>(R.id.arrowFl)?.visibility = View.GONE
                val checkbox = v.findViewById<ImageButton>(R.id.checkboxIb)
                checkbox?.visibility = View.VISIBLE
                checkbox.setOnClickListener {
                    setSelected(checkbox, folder)
                }
                v.setOnClickListener {
                    setSelected(checkbox, folder)
                }
            } else {
                v.setOnClickListener { presenter.openFolder(folder) }
            }
            foldersLl.addView(v)


            if (folder.folders.isNotEmpty()) {
                processFoldersRecursive(folder.folders, level + 1)
            }
        }
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
        if (!show){
            animateView()
        }
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showEmpty(show: Boolean) {
        emptyFl.visibility = if (show) View.VISIBLE else View.GONE
        refreshSrl.visibility = if (!show) View.VISIBLE else View.GONE
    }
}