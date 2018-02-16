package dk.eboks.app.presentation.ui.screens.debug

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import dk.eboks.app.BuildConfig
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hint.*

class ComponentTestActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_component_test)

        // we dont need stinkin injection here
        //component.inject(this)

    }

    override fun setupTranslations() {

    }
}
