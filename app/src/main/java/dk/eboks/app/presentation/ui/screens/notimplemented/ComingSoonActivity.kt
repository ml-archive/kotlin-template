package dk.eboks.app.presentation.ui.screens.notimplemented

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.navigation.NavBarComponentFragment
import kotlinx.android.synthetic.main.include_toolbar.*

class ComingSoonActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(NavBarComponentFragment.currentMenuItem == R.id.actionUploads) {
            setContentView(dk.eboks.app.R.layout.activity_coming_soon_uploads)
            mainTb.title = Translation.uploads.title
        }
        if(NavBarComponentFragment.currentMenuItem == R.id.actionSenders) {
            setContentView(dk.eboks.app.R.layout.activity_coming_soon_senders)
            mainTb.title = Translation.senders.title
        }
    }

}
