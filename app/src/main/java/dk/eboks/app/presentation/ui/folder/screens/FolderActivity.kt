package dk.eboks.app.presentation.ui.folder.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.folder.components.FoldersComponentFragment
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentFragment
import dk.eboks.app.util.guard
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.activity_folder.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class FolderActivity : BaseActivity(), FolderContract.View {
    @Inject lateinit var presenter: FolderContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
        val frag = FoldersComponentFragment()
        intent?.extras?.let { extras ->
            Timber.d("${extras.getBoolean(ARG_OVERIDE_USER)}")
            frag.putArg("override_user", extras.getBoolean(ARG_OVERIDE_USER, false))
            if (extras.containsKey("pick")) {
                frag.putArg("pick", true)
            }
            navBarContainer.visibility = View.GONE
            removeNavBarMargin()
        }.guard {
            // if not pickmode, then nav bar should be shown
            navBarContainer.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction().add(
                R.id.navBarContainer,
                NavBarComponentFragment(),
                NavBarComponentFragment::class.java.simpleName
            ).commit()
        }
        setRootFragment(R.id.foldersFl, frag)
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
    }

    private fun removeNavBarMargin() {
        var params = CoordinatorLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val tv = TypedValue()
        var actionBarHeight = 0
        if (this.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true))
            run {
                actionBarHeight =
                    TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            }
        params.setMargins(0, actionBarHeight, 0, 0)
        foldersContainerFl.layoutParams = params
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = Translation.folders.foldersHeader
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun getNavigationMenuAction(): Int {
        return R.id.actionMail
    }

    companion object {
        val REQUEST_ID: Int = 2468
        const val ARG_OVERIDE_USER = "override_user"

        fun startAsIntent(context: Context?, overrideCurrentUser: Boolean = false) {
            val intent = Intent(context, FolderActivity::class.java).apply {
                putExtra(ARG_OVERIDE_USER, overrideCurrentUser)
            }
            context?.startActivity(intent)
        }
    }
}
