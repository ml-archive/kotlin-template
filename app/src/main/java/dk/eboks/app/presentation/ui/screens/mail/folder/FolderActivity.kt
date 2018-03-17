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
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.ic_menu_mail)
        mainTb.title = Translation.folders.foldersHeader
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
        setupTopBar()
    }


}
