package dk.eboks.app.presentation.ui.start.screens

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * Created by Christian on 5/30/2018.
 * @author Christian
 * @since 5/30/2018.
 */
class HelpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation_help)

        mainTb.setNavigationIcon(R.drawable.ic_red_close)
        mainTb.title = Translation.findactivationcode.title
        mainTb.setNavigationOnClickListener {
            finish()
        }
    }
}