package dk.eboks.app.presentation.ui.screens.debug

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dk.eboks.app.util.ClassLoaderUtil
import kotlinx.android.synthetic.main.activity_debug.*
import kotlinx.android.synthetic.main.include_toolnar.*
import timber.log.Timber

class DebugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_debug)

        //component.inject(this)
        toolbarTv.text = "Debug"

        componentTestTv.setOnClickListener {
            startActivity(Intent(this, ComponentTestActivity::class.java))
        }

    }


}
