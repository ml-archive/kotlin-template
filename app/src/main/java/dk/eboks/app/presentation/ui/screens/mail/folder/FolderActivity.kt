package dk.eboks.app.presentation.ui.screens.mail.folder

import android.os.Bundle
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
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
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.red_navigationbar)
        mainTb.title = Translation.folders.foldersHeader
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
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

    }

    override fun getNavigationMenuAction(): Int { return R.id.actionMail }
}
