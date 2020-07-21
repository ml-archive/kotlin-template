package dk.nodes.template.presentation.ui.base

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import dk.nodes.nstack.kotlin.inflater.NStackBaseContext

abstract class BaseActivity : AppCompatActivity {

    constructor() : super()
    constructor(@LayoutRes resId: Int) : super(resId)

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(NStackBaseContext(newBase))
    }
}


