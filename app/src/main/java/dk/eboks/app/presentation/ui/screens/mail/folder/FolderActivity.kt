package dk.eboks.app.presentation.ui.screens.mail.folder

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.folder.folders.FoldersComponentFragment
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.activity_folder.*
import kotlinx.android.synthetic.main.include_toolbar.*
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
        intent?.extras?.let { extras->
            if(extras.containsKey("pick")) { frag.putArg("pick", true) }
        }
        setRootFragment(R.id.foldersComponentFragment, frag)
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = Translation.folders.foldersHeader
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    override fun getNavigationMenuAction(): Int { return R.id.actionMail }

    companion object {
        val REQUEST_ID: Int = 2468
    }
}
